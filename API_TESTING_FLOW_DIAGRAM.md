# EduSphere API Testing Flow Diagram

## 📊 Complete Testing Workflow

```
START
  │
  ├─→ PHASE 1: Authentication Setup
  │    │
  │    ├─→ Register Admin User
  │    │    ├─ POST /auth/register
  │    │    └─ Save: ADMIN_ACCESS_TOKEN
  │    │
  │    ├─→ Register Student User
  │    │    ├─ POST /auth/register
  │    │    └─ Save: STUDENT_ACCESS_TOKEN
  │    │
  │    ├─→ Register Faculty User
  │    │    ├─ POST /auth/register
  │    │    └─ Save: FACULTY_ACCESS_TOKEN
  │    │
  │    └─→ Register Department Head User
  │         ├─ POST /auth/register
  │         ├─ Save: DEPTHEAD_ACCESS_TOKEN
  │         └─ Save: DEPTHEAD_USER_ID
  │
  ├─→ PHASE 2: Department Setup
  │    │
  │    ├─→ Create CS Department
  │    │    ├─ POST /departments
  │    │    ├─ Authorization: ADMIN_ACCESS_TOKEN
  │    │    └─ Save: DEPT_CS_ID
  │    │
  │    ├─→ Create IT Department
  │    │    ├─ POST /departments
  │    │    ├─ Authorization: ADMIN_ACCESS_TOKEN
  │    │    └─ Save: DEPT_IT_ID
  │    │
  │    ├─→ Get All Departments
  │    │    └─ GET /departments
  │    │
  │    ├─→ Get CS Department Details
  │    │    └─ GET /departments/{DEPT_CS_ID}
  │    │
  │    ├─→ Update CS Department
  │    │    └─ PUT /departments/{DEPT_CS_ID}
  │    │
  │    ├─→ Partial Update CS Department
  │    │    └─ PATCH /departments/{DEPT_CS_ID}
  │    │
  │    ├─→ Assign Department Head to CS
  │    │    ├─ PATCH /departments/{DEPT_CS_ID}/head
  │    │    └─ Send: DEPTHEAD_USER_ID
  │    │
  │    └─→ Get Faculty in CS Department
  │         └─ GET /departments/{DEPT_CS_ID}/faculty
  │
  ├─→ PHASE 3: Faculty Setup
  │    │
  │    ├─→ Create Faculty 1
  │    │    ├─ POST /faculties
  │    │    ├─ Department: DEPT_CS_ID
  │    │    └─ Save: FACULTY_ID_1
  │    │
  │    ├─→ Create Faculty 2
  │    │    ├─ POST /faculties
  │    │    ├─ Department: DEPT_CS_ID
  │    │    └─ Save: FACULTY_ID_2
  │    │
  │    ├─→ Get All Faculties
  │    │    └─ GET /faculties
  │    │
  │    ├─→ Get Faculty 1 Details
  │    │    └─ GET /faculties/{FACULTY_ID_1}
  │    │
  │    ├─→ Update Faculty 1 (Full)
  │    │    └─ PUT /faculties/{FACULTY_ID_1}
  │    │
  │    ├─→ Update Faculty 1 (Partial)
  │    │    └─ PATCH /faculties/{FACULTY_ID_1}
  │    │
  │    └─→ Delete Faculty 2
  │         └─ DELETE /faculties/{FACULTY_ID_2}
  │
  ├─→ PHASE 4: Student Setup
  │    │
  │    ├─→ Create Student 1
  │    │    ├─ POST /students
  │    │    └─ Save: STUDENT_ID_1
  │    │
  │    ├─→ Create Student 2
  │    │    ├─ POST /students
  │    │    └─ Save: STUDENT_ID_2
  │    │
  │    ├─→ Get All Students
  │    │    └─ GET /students
  │    │
  │    ├─→ Get Student 1 Details
  │    │    └─ GET /students/{STUDENT_ID_1}
  │    │
  │    ├─→ Update Student 1 (Full)
  │    │    └─ PUT /students/{STUDENT_ID_1}
  │    │
  │    ├─→ Update Student 1 (Partial)
  │    │    └─ PATCH /students/{STUDENT_ID_1}
  │    │
  │    └─→ Delete Student 2
  │         └─ DELETE /students/{STUDENT_ID_2}
  │
  └─→ TESTING COMPLETE ✅
```

---

## 🔐 Authentication Flow

```
User Input
  │
  ├─→ POST /auth/register
  │    │
  │    ├─→ Validate Input
  │    │    ├─ Email format
  │    │    ├─ Phone format
  │    │    ├─ Password strength
  │    │    └─ Role validity
  │    │
  │    ├─→ Create User Entity
  │    │    ├─ Encode password
  │    │    ├─ Set role
  │    │    └─ Set status
  │    │
  │    ├─→ Save to Database
  │    │    └─ INSERT INTO users
  │    │
  │    ├─→ Generate JWT Tokens
  │    │    ├─ Access Token (15 min)
  │    │    └─ Refresh Token (longer)
  │    │
  │    └─→ Response (201 Created)
  │         ├─ accessToken
  │         └─ refreshToken
  │
  └─→ Store Tokens Securely
```

---

## 📊 Department Entity Relationship

```
                        ┌──────────────┐
                        │ DepartmentHead
                        │  (Role: HEAD) │
                        └────────┬─────┘
                                 │
                            1-to-1│
                                 │
        ┌────────────────────────▼────────────────────────┐
        │          Department                             │
        │  ─────────────────────                          │
        │  - id: UUID                                    │
        │  - departmentName: String                      │
        │  - departmentCode: String (unique)             │
        │  - contactInfo: String                         │
        │  - status: Status enum                         │
        │  - head: DepartmentHead (optional)             │
        │  - faculties: List<Faculty> (1-to-many)       │
        │  - courses: List<Course> (1-to-many)          │
        │  - createdAt: Instant                          │
        │  - updatedAt: Instant                          │
        └────────────────┬─────────────────────┬─────────┘
                         │                     │
                    1-to-many             1-to-many
                         │                     │
        ┌────────────────▼─┐      ┌───────────▼────────┐
        │   Faculty        │      │     Course         │
        │  ────────        │      │     ──────         │
        │  - id: UUID      │      │  - id: UUID        │
        │  - name: String  │      │  - name: String    │
        │  - position      │      │  - code: String    │
        │  - joinDate      │      │  - credits: Int    │
        │  - department_id │      │  - department_id   │
        └──────────────────┘      └────────────────────┘
```

---

## 👥 User Inheritance Hierarchy

```
                    ┌─────────────────┐
                    │   BaseEntity    │
                    │  ─────────────  │
                    │  - id: UUID     │
                    │  - createdAt    │
                    │  - updatedAt    │
                    └────────┬────────┘
                             │
                             │ extends
                             │
                    ┌────────▼────────────┐
                    │       User          │
                    │  ──────────────     │
                    │  - name: String     │
                    │  - email: String    │
                    │  - phone: String    │
                    │  - password: String │
                    │  - role: Role enum  │
                    │  - status: Status   │
                    └──┬──────────┬──────┬┘
                       │          │      │
         ┌─────────────┘  │        │      │
         │                │        │      │
    ┌────▼────┐      ┌────▼──┐ ┌─▼──┐ ┌─▼──────────┐
    │ Student │      │Faculty│ │Head│ │ Compliance │
    │ ─────── │      │────── │ │────│ │ Officer    │
    │ - dob   │      │-dept  │ │-id │ │ ────────   │
    │ - gender│      │-pos   │ │    │ │ - audits   │
    │ - addr  │      │-joined│ │    │ │ - records  │
    │ - enroll│      │       │ │    │ │            │
    └────────┘      └───────┘ └────┘ └────────────┘
```

---

## 🔄 Request Processing Pipeline

```
HTTP Request
    │
    ├─→ Servlet Container
    │    └─ Route to Spring DispatcherServlet
    │
    ├─→ Security Filters
    │    ├─ JwtAuthenticationFilter
    │    │   ├─ Extract JWT from Authorization header
    │    │   ├─ Validate token signature
    │    │   ├─ Check token expiration
    │    │   └─ Extract user principal
    │    │
    │    └─ Spring Security Chain
    │        ├─ Check authentication
    │        ├─ Check authorization
    │        └─ Load user roles
    │
    ├─→ Controller Layer
    │    ├─ Receive request
    │    ├─ Parse path variables
    │    ├─ Parse request body
    │    └─ Call service method
    │
    ├─→ Validation Layer
    │    ├─ @NotNull, @NotBlank
    │    ├─ @Email, @Pattern
    │    ├─ @PastOrPresent, @Size
    │    └─ Throw exception if invalid
    │
    ├─→ Service Layer
    │    ├─ Business logic
    │    ├─ Call repository
    │    └─ Data transformation
    │
    ├─→ Repository Layer
    │    ├─ Build query
    │    ├─ Execute SQL
    │    └─ Return entities
    │
    ├─→ Database
    │    ├─ Query execution
    │    ├─ Data retrieval/persistence
    │    └─ Transaction management
    │
    ├─→ Response Building
    │    ├─ Convert entity to DTO
    │    ├─ Set HTTP status
    │    └─ Build response body
    │
    └─→ HTTP Response
        ├─ Status code (200, 201, 404, etc)
        ├─ Headers
        ├─ JSON body
        └─ Send to client
```

---

## 🧪 Test Sequence Timeline

```
Time    | Action                        | Status
--------|-------------------------------|--------
T+0s    | Server Started                | ✅
T+5s    | Register Admin                | ✅ Token saved
T+10s   | Register Student              | ✅ Token saved
T+15s   | Register Faculty              | ✅ Token saved
T+20s   | Register Dept Head            | ✅ Token saved
T+25s   | Create CS Dept                | ✅ ID saved
T+30s   | Create IT Dept                | ✅ ID saved
T+35s   | Get All Depts                 | ✅ Verified
T+40s   | Get CS Dept                   | ✅ Verified
T+45s   | Update CS Dept                | ✅ Verified
T+50s   | Partial Update Dept           | ✅ Verified
T+55s   | Assign Dept Head              | ✅ Verified
T+60s   | Get Faculty in Dept           | ✅ Verified
T+65s   | Create Faculty 1              | ✅ ID saved
T+70s   | Create Faculty 2              | ✅ ID saved
T+75s   | Get All Faculties             | ✅ Verified
T+80s   | Get Faculty Details           | ✅ Verified
T+85s   | Update Faculty (Full)         | ✅ Verified
T+90s   | Update Faculty (Partial)      | ✅ Verified
T+95s   | Delete Faculty                | ✅ Verified
T+100s  | Create Student 1              | ✅ ID saved
T+105s  | Create Student 2              | ✅ ID saved
T+110s  | Get All Students              | ✅ Verified
T+115s  | Get Student Details           | ✅ Verified
T+120s  | Update Student (Full)         | ✅ Verified
T+125s  | Update Student (Partial)      | ✅ Verified
T+130s  | Delete Student                | ✅ Verified
--------|-------------------------------|--------
T+135s  | All Tests Complete            | ✅ PASSED
```

---

## 📈 Data Growth During Testing

```
After Phase 1 (Auth):
├─ Users: 4 (Admin, Student, Faculty, DeptHead)
└─ Tokens: 4

After Phase 2 (Departments):
├─ Users: 4
├─ Departments: 2 (CS, IT)
└─ Relationships: 1 (DeptHead → CS)

After Phase 3 (Faculty):
├─ Users: 4
├─ Departments: 2
├─ Faculty: 2
└─ Relationships: 2 (Faculty → CS)

After Phase 4 (Students):
├─ Users: 4
├─ Departments: 2
├─ Faculty: 1 (Faculty 2 deleted)
├─ Students: 1 (Student 2 deleted)
└─ Total Entities: 8
```

---

## 🔍 Error Handling Flow

```
Request Received
    │
    ├─→ Validation Phase
    │    │
    │    ├─ Input valid?
    │    │   └─ NO → 400 Bad Request
    │    │        ├─ Return error message
    │    │        └─ Log error
    │    │
    │    └─ YES → Continue
    │
    ├─→ Authentication Phase
    │    │
    │    ├─ Has Authorization header?
    │    │   └─ NO → 401 Unauthorized
    │    │
    │    ├─ Token valid?
    │    │   └─ NO → 401 Unauthorized
    │    │
    │    ├─ Token not expired?
    │    │   └─ NO → 401 Unauthorized
    │    │
    │    └─ YES → Continue
    │
    ├─→ Authorization Phase
    │    │
    │    ├─ User has permission?
    │    │   └─ NO → 403 Forbidden
    │    │
    │    └─ YES → Continue
    │
    ├─→ Business Logic Phase
    │    │
    │    ├─ Resource exists?
    │    │   └─ NO → 404 Not Found
    │    │
    │    ├─ Data constraint violation?
    │    │   └─ NO → Continue
    │    │   └─ YES → 409 Conflict
    │    │
    │    └─ Unexpected error?
    │        └─ YES → 500 Internal Server Error
    │
    └─→ Success → 200/201 OK/Created
```

---

## 🗄️ Database Transaction Flow

```
Request Starts Transaction
    │
    ├─→ @Transactional
    │    └─ Begin transaction
    │
    ├─→ Service Method
    │    │
    │    ├─→ Read operation
    │    │    ├─ Query executed
    │    │    └─ Data retrieved
    │    │
    │    ├─→ Write operation
    │    │    ├─ Entity modified
    │    │    └─ Repository.save()
    │    │
    │    └─→ Another operation
    │         └─ Continuing...
    │
    ├─→ All operations complete
    │    │
    │    ├─ Exception thrown?
    │    │   └─ YES → Rollback
    │    │   └─ NO → Commit
    │    │
    │    └─ Transaction ends
    │
    └─→ Response sent
```

---

## 🎯 Success Criteria

### Phase 1: Authentication ✅
```
[✓] Admin registered & token obtained
[✓] Student registered & token obtained
[✓] Faculty registered & token obtained
[✓] DeptHead registered & token obtained
```

### Phase 2: Departments ✅
```
[✓] CS Department created
[✓] IT Department created
[✓] All departments retrieved
[✓] Department by ID retrieved
[✓] Department updated
[✓] Department partially updated
[✓] Department head assigned
[✓] Faculty in department listed
```

### Phase 3: Faculties ✅
```
[✓] Faculty 1 created
[✓] Faculty 2 created
[✓] All faculties retrieved
[✓] Faculty by ID retrieved
[✓] Faculty updated (full)
[✓] Faculty updated (partial)
[✓] Faculty deleted
```

### Phase 4: Students ✅
```
[✓] Student 1 created
[✓] Student 2 created
[✓] All students retrieved
[✓] Student by ID retrieved
[✓] Student updated (full)
[✓] Student updated (partial)
[✓] Student deleted
```

---

## 📱 Postman Setup

### Collections Structure
```
EduSphere API Testing Collection
├── PHASE 1: AUTHENTICATION
│   ├── 1.1 Register Admin User
│   ├── 1.2 Register Student User
│   ├── 1.3 Register Faculty User
│   ├── 1.4 Register Department Head
│   ├── 1.5 Login
│   └── 1.6 Refresh Token
├── PHASE 2: DEPARTMENTS
│   ├── 2.1 Create Department (CS)
│   ├── 2.2 Create Department (IT)
│   ├── 2.3 Get All Departments
│   ├── 2.4 Get Department by ID
│   ├── 2.5 Update Department (PUT)
│   ├── 2.6 Partial Update (PATCH)
│   ├── 2.7 Assign Department Head
│   ├── 2.8 Get Faculty in Department
│   └── 2.9 Delete Department
├── PHASE 3: FACULTIES
│   ├── 3.1 Create Faculty 1
│   ├── 3.2 Create Faculty 2
│   ├── 3.3 Get All Faculties
│   ├── 3.4 Get Faculty by ID
│   ├── 3.5 Update Faculty (PUT)
│   ├── 3.6 Partial Update (PATCH)
│   └── 3.7 Delete Faculty
└── PHASE 4: STUDENTS
    ├── 4.1 Create Student 1
    ├── 4.2 Create Student 2
    ├── 4.3 Get All Students
    ├── 4.4 Get Student by ID
    ├── 4.5 Update Student (PUT)
    ├── 4.6 Partial Update (PATCH)
    └── 4.7 Delete Student
```

### Environment Variables
```
{{BASE_URL}} = http://localhost:8081
{{ADMIN_ACCESS_TOKEN}} = <token from 1.1>
{{STUDENT_ACCESS_TOKEN}} = <token from 1.2>
{{FACULTY_ACCESS_TOKEN}} = <token from 1.3>
{{DEPTHEAD_ACCESS_TOKEN}} = <token from 1.4>
{{DEPTHEAD_USER_ID}} = <id from 1.4>
{{DEPT_CS_ID}} = <id from 2.1>
{{DEPT_IT_ID}} = <id from 2.2>
{{FACULTY_ID_1}} = <id from 3.1>
{{FACULTY_ID_2}} = <id from 3.2>
{{STUDENT_ID_1}} = <id from 4.1>
{{STUDENT_ID_2}} = <id from 4.2>
```

---

## 🎬 Example: Create Department Flow

```
HTTP Request
┌─────────────────────────────────────────┐
│ POST /api/v1/departments                │
│ Authorization: Bearer {{ADMIN_TOKEN}}   │
│ Content-Type: application/json          │
│                                         │
│ {                                       │
│   "departmentName": "Computer Science", │
│   "departmentCode": "CS",               │
│   "contactInfo": "cs@university.edu",   │
│   "status": "ACTIVE",                   │
│   "headId": null                        │
│ }                                       │
└─────────────────────────────────────────┘
         │
         ▼
    Spring Processes Request
         │
         ├─→ JwtAuthenticationFilter
         │    ├─ Extract token
         │    ├─ Validate: eyJ...
         │    └─ Extract userId, role
         │
         ├─→ DepartmentController
         │    └─ POST /departments
         │
         ├─→ Validation
         │    ├─ departmentName: @NotBlank ✓
         │    ├─ departmentCode: @NotBlank ✓
         │    ├─ contactInfo: @NotBlank ✓
         │    └─ status: @NotNull ✓
         │
         ├─→ DepartmentService
         │    └─ createDepartment()
         │
         ├─→ DepartmentRepository
         │    └─ save(department)
         │
         ├─→ Hibernate ORM
         │    └─ Convert to SQL
         │
         ├─→ MySQL
         │    └─ INSERT INTO department
         │       VALUES (...)
         │
         ├─→ Response Building
         │    ├─ HTTP 201 Created
         │    ├─ Set Location header
         │    └─ Serialize to JSON
         │
         ▼
HTTP Response
┌──────────────────────────────────────────────────┐
│ HTTP/1.1 201 Created                             │
│ Location: /api/v1/departments/123-uuid           │
│ Content-Type: application/json                   │
│                                                  │
│ {                                                │
│   "id": "550e8400-e29b-41d4-a716-446655440000", │
│   "departmentName": "Computer Science",          │
│   "departmentCode": "CS",                        │
│   "contactInfo": "cs@university.edu",            │
│   "status": "ACTIVE",                            │
│   "head": null,                                  │
│   "createdAt": "2026-03-03T15:30:00Z",           │
│   "updatedAt": "2026-03-03T15:30:00Z"            │
│ }                                                │
└──────────────────────────────────────────────────┘
         │
         ▼
    Save in Postman
    DEPT_CS_ID = 550e8400-e29b-41d4-a716-446655440000
```

---

## 📞 Troubleshooting Decision Tree

```
Application won't start
├─ Check port 8081
│  ├─ Kill process: taskkill /PID <PID> /F
│  └─ Restart application
├─ Check MySQL
│  ├─ Verify service running
│  ├─ Check credentials
│  └─ Verify database exists
└─ Check logs for errors

API returns 400 Bad Request
├─ Check JSON syntax
├─ Verify enum values
│  ├─ Status: ACTIVE, INACTIVE, ARCHIVED
│  ├─ Gender: MALE, FEMALE, OTHER
│  └─ Role: STUDENT, FACULTY, HEAD, ADMIN
├─ Verify all required fields
└─ Check field formats

API returns 401 Unauthorized
├─ Check Authorization header exists
├─ Verify token format: Bearer <token>
├─ Check token hasn't expired
└─ Register new user for new token

API returns 404 Not Found
├─ Verify resource ID exists
├─ Check ID matches from previous response
├─ Try GET all endpoint first
└─ Create resource if needed

API returns 500 Internal Server Error
├─ Check server console logs
├─ Verify database connection
├─ Check for constraint violations
└─ Restart application
```

---

This visual guide helps understand the complete flow of testing all APIs in sequence!


