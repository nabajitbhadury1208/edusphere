# EduSphere — Frontend Integration Guide

> **Last Updated:** 2026-04-07  
> **Base URL:** `http://localhost:9086/api/v1`  
> **Auth:** All endpoints require `Authorization: Bearer <access_token>` header except `/auth/*`

---

## Role Values (exact strings in JWT `roles` claim)

| Role | String value used in JWT |
|------|--------------------------|
| Administrator | `ADMIN` |
| Faculty Member | `FACULTY` |
| Student | `STUDENT` |
| Department Head | `DEPARTMENT_HEAD` |
| Compliance Officer | `COMPLIANCE_OFFICER` |
| Regulator | `REGULATOR` |

---

## Common Response Shape

All error responses follow:
```json
{ "status": 403, "error": "Forbidden", "message": "Access Denied" }
{ "status": 404, "error": "Not Found",  "message": "..." }
{ "status": 500, "error": "Internal Server Error", "message": "..." }
```

---

## ✅ Changes Made (What the Frontend Must Update)

| Module | What Changed |
|--------|-------------|
| **Grades** | Added `GET /grades/my` for students; `GET /grades` now includes `FACULTY`; fixed `COMPLIANCE` → `COMPLIANCE_OFFICER` role; IDOR protection on `/students/{studentId}` |
| **Thesis** | Added `GET /thesis/my` (student + faculty scoped); added `GET /thesis` (admin only); IDOR protection on all read endpoints; `POST /thesis` auto-injects `studentId` from JWT for students |
| **Faculty** | `GET /faculties` and `GET /faculties/{id}` now accessible to `STUDENT` (needed for supervisor name display and dropdown) |

---

## Grades API

**Base path:** `/api/v1/grades`

### Request Body — `GradeRequest`
```json
{
  "examId":    "uuid",        // required on create
  "studentId": "uuid",        // required on create
  "score":     90.0,          // 0.0–100.0, required on create
  "grade":     "A",           // required on update
  "status":    "PASS"         // PASS | FAIL | PENDING — required on create
}
```

### Response Body — `GradeResponse`
```json
{
  "id":        "uuid",
  "examId":    "uuid",
  "studentId": "uuid",
  "score":     90.0,
  "grade":     "A",
  "status":    "PASS",
  "createdAt": "2026-04-07T10:00:00Z",
  "updatedAt": "2026-04-07T10:00:00Z"
}
```

### Endpoints

| # | Method | Endpoint | Description | Who Can Call |
|---|--------|----------|-------------|--------------|
| 1 | `POST` | `/grades` | Submit a grade | `ADMIN`, `FACULTY` |
| 2 | `GET` | `/grades` | Get all grades | `ADMIN`, `FACULTY`, `COMPLIANCE_OFFICER` |
| 3 | `GET` | `/grades/{id}` | Get a grade by id | `ADMIN`, `FACULTY`, `COMPLIANCE_OFFICER` |
| 4 | `GET` | `/grades/my` | ⭐ Get grades of the **logged-in student** (JWT-scoped) | `STUDENT` only |
| 5 | `GET` | `/grades/students/{studentId}` | Get all grades of a student | `ADMIN`, `FACULTY`, `DEPARTMENT_HEAD`, `COMPLIANCE_OFFICER`, `STUDENT` (own only) |
| 6 | `GET` | `/grades/exam/{examId}` | Get all grades for an exam | `ADMIN`, `FACULTY`, `COMPLIANCE_OFFICER` |
| 7 | `PUT` | `/grades/{id}` | Update a grade | `ADMIN`, `FACULTY` |
| 8 | `DELETE` | `/grades/{id}` | Delete a grade | `ADMIN` |

### Frontend Rules by Role

| Role | Use This Endpoint |
|------|-------------------|
| `STUDENT` | Always call `GET /grades/my` — never pass `studentId` manually |
| `FACULTY` | `GET /grades` (all), `GET /grades/students/{studentId}` (per student) |
| `ADMIN` | All endpoints |
| `COMPLIANCE_OFFICER` | `GET /grades`, `GET /grades/{id}`, `GET /grades/students/{studentId}`, `GET /grades/exam/{examId}` |
| `DEPARTMENT_HEAD` | `GET /grades/students/{studentId}` only |

> ⚠️ **IDOR Protection:** `STUDENT` calling `GET /grades/students/{studentId}` will get `403` if `studentId` ≠ their own `userId`. Use `GET /grades/my` instead.

---

## Thesis API

**Base path:** `/api/v1/thesis`

### Request Body — `ThesisRequestDto`

**When called by `STUDENT`** — `studentId` is **ignored** (auto-set from JWT):
```json
{
  "title":          "My Thesis Title",
  "supervisorId":   "uuid",           // faculty UUID — required
  "submissionDate": "2026-06-30",     // LocalDate ISO format
  "status":         "IN_PROGRESS"     // see ThesisStatus enum below
}
```

**When called by `ADMIN` or `FACULTY`** — `studentId` must be provided:
```json
{
  "studentId":      "uuid",           // required
  "title":          "Thesis Title",
  "supervisorId":   "uuid",           // required
  "submissionDate": "2026-06-30",
  "status":         "IN_PROGRESS"
}
```

**`ThesisStatus` enum values:** `IN_PROGRESS` | `SUBMITTED` | `APPROVED` | `REJECTED`

### Response Body — `ThesisResponseDto`
```json
{
  "id":             "uuid",
  "studentId":      "uuid",
  "title":          "My Thesis Title",
  "supervisorId":   "uuid",
  "submissionDate": "2026-06-30",
  "status":         "IN_PROGRESS"
}
```

### Endpoints

| # | Method | Endpoint | Description | Who Can Call |
|---|--------|----------|-------------|--------------|
| 1 | `POST` | `/thesis` | Create a thesis | `ADMIN`, `FACULTY`, `STUDENT` |
| 2 | `GET` | `/thesis` | Get **all** theses | `ADMIN` only |
| 3 | `GET` | `/thesis/my` | ⭐ Get theses for the **logged-in user** (JWT-scoped) | `STUDENT` (own), `FACULTY` (as supervisor) |
| 4 | `GET` | `/thesis/{id}` | Get a thesis by id | `ADMIN`, `DEPARTMENT_HEAD`, `COMPLIANCE_OFFICER`, `STUDENT` (if assigned), `FACULTY` (if supervisor) |
| 5 | `GET` | `/thesis/student/{studentId}` | Get all theses for a student | `ADMIN`, `FACULTY`, `DEPARTMENT_HEAD`, `COMPLIANCE_OFFICER`, `STUDENT` (own only) |
| 6 | `GET` | `/thesis/supervisor/{facultyId}` | Get all theses supervised by a faculty | `ADMIN`, `DEPARTMENT_HEAD`, `COMPLIANCE_OFFICER`, `FACULTY` (own only) |
| 7 | `PUT` | `/thesis/{id}` | Update a thesis | `ADMIN`, `FACULTY` |
| 8 | `DELETE` | `/thesis/{id}` | Delete a thesis | `ADMIN` |

### Frontend Rules by Role

| Role | Use This Endpoint | Notes |
|------|-------------------|-------|
| `STUDENT` | `GET /thesis/my` to list, `POST /thesis` to submit | Do **not** send `studentId` in the POST body — backend ignores it and uses JWT |
| `FACULTY` | `GET /thesis/my` to see supervised theses | Returns only thesis where they are the supervisor |
| `ADMIN` | `GET /thesis` for full list | All endpoints available |
| `DEPARTMENT_HEAD` | `GET /thesis/student/{studentId}`, `GET /thesis/supervisor/{facultyId}` | Read-only |
| `COMPLIANCE_OFFICER` | Same as `DEPARTMENT_HEAD` | Read-only |

> ⚠️ **IDOR Protection:**
> - `GET /thesis/{id}` — returns `403` if student is not the assigned student, or if faculty is not the supervisor
> - `GET /thesis/student/{studentId}` — `STUDENT` gets `403` if `studentId` ≠ their own `userId`
> - `GET /thesis/supervisor/{facultyId}` — `FACULTY` gets `403` if `facultyId` ≠ their own `userId`

---

## Faculty API

**Base path:** `/api/v1/faculties`

> ℹ️ **Why students need this:** Students call `GET /faculties` to populate the supervisor dropdown when submitting a new thesis, and `GET /faculties/{id}` to display supervisor names on the thesis list page.

### Response Body — `FacultyResponseDTO`
```json
{
  "id":           "uuid",
  "name":         "Dr. John Smith",
  "email":        "john.smith@edu.com",
  "phone":        "9876543210",
  "departmentId": "uuid",
  "position":     "Professor",
  "joinDate":     "2020-01-15",
  "status":       "ACTIVE"
}
```

### Endpoints

| # | Method | Endpoint | Description | Who Can Call |
|---|--------|----------|-------------|--------------|
| 1 | `POST` | `/faculties` | Create a faculty | `ADMIN` |
| 2 | `GET` | `/faculties` | Get all faculties | `ADMIN`, `DEPARTMENT_HEAD`, `STUDENT` ⭐ |
| 3 | `GET` | `/faculties/{id}` | Get a faculty by id | `ADMIN`, `DEPARTMENT_HEAD`, `STUDENT` ⭐, `FACULTY` (own only) |
| 4 | `PUT` | `/faculties/{id}` | Update a faculty | `ADMIN` |
| 5 | `DELETE` | `/faculties/{id}` | Delete a faculty | `ADMIN` |

> ⭐ **Changed:** `STUDENT` was previously blocked (403) on both `GET /faculties` and `GET /faculties/{id}`. Both now allow `STUDENT` access.

---

## Grades — Per-Role Frontend Flow

### Student (`STUDENT`)
```
Page load:
  1. GET /grades/my                        → list of GradeResponse[]
  2. For each unique examId:
     GET /exams/{examId}                   → ExamResponse (has courseId)
  3. For each unique courseId:
     GET /courses/{courseId}               → CourseResponse (has title)
  Display: course title | exam type | score | grade | status
```

### Faculty (`FACULTY`)
```
Page load:
  GET /grades                              → all grades (FACULTY allowed ✅)
  GET /grades/students/{studentId}         → grades for a specific student
  POST /grades                             → submit a grade
  PUT /grades/{id}                         → update a grade
```

### Admin (`ADMIN`)
```
All endpoints available — no restrictions
```

### Compliance Officer (`COMPLIANCE_OFFICER`)
```
GET /grades                               → all grades
GET /grades/{id}                          → single grade
GET /grades/students/{studentId}          → student's grades
GET /grades/exam/{examId}                 → exam's grades
No create/update/delete
```

---

## Thesis — Per-Role Frontend Flow

### Student (`STUDENT`)
```
Page load:
  1. GET /thesis/my                        → ThesisResponseDto[] (own only)
  2. GET /faculties                        → for supervisor name lookup & dropdown

Submit new thesis modal:
  POST /thesis
  Body: { title, supervisorId, submissionDate, status }
  ⚠️ Do NOT send studentId — backend sets it from JWT automatically
```

### Faculty (`FACULTY`)
```
Page load (supervision view):
  GET /thesis/my                           → ThesisResponseDto[] (only supervised)

Update thesis status:
  PUT /thesis/{id}
  Body: { studentId, title, supervisorId, submissionDate, status }
```

### Admin (`ADMIN`)
```
GET /thesis                               → all theses
POST /thesis                              → create (must include studentId)
PUT /thesis/{id}                          → update
DELETE /thesis/{id}                       → delete
```

### Department Head (`DEPARTMENT_HEAD`)
```
GET /thesis/student/{studentId}           → view student's theses
GET /thesis/supervisor/{facultyId}        → view supervisor's theses
GET /thesis/{id}                          → view a single thesis
Read-only access
```

---

## Enums Reference

### `GradeStatus`
| Value | Meaning |
|-------|---------|
| `PASS` | Student passed |
| `FAIL` | Student failed |
| `PENDING` | Not yet evaluated |

### `ThesisStatus`
| Value | Meaning |
|-------|---------|
| `IN_PROGRESS` | Being written |
| `SUBMITTED` | Submitted for review |
| `APPROVED` | Approved by supervisor/admin |
| `REJECTED` | Rejected |

### `ExamType` (from ExamResponse)
| Value | Meaning |
|-------|---------|
| `MIDTERM` | Mid-semester exam |
| `FINAL` | Final exam |
| `QUIZ` | Short quiz |
| `ASSIGNMENT` | Assignment-based |

---

## Quick Reference — What Endpoint to Call by Page

| Page | Role | Call |
|------|------|------|
| My Grades | `STUDENT` | `GET /grades/my` |
| Grade Submission | `FACULTY` | `POST /grades` |
| All Grades List | `FACULTY`, `ADMIN`, `COMPLIANCE_OFFICER` | `GET /grades` |
| Student Grades (admin view) | `ADMIN`, `FACULTY` | `GET /grades/students/{studentId}` |
| My Thesis | `STUDENT` | `GET /thesis/my` |
| Thesis Supervision | `FACULTY` | `GET /thesis/my` |
| All Thesis (admin) | `ADMIN` | `GET /thesis` |
| Submit Thesis | `STUDENT` | `POST /thesis` (no studentId in body) |
| Supervisor Dropdown | `STUDENT` | `GET /faculties` |
| Supervisor Name Display | `STUDENT` | `GET /faculties/{id}` |

