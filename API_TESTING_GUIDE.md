# API Testing Guide - EduSphere

## Prerequisites
- Application running on `http://localhost:8081`
- MySQL database running with `edusphere` database created
- Postman or curl installed

## Base URL
```
http://localhost:8081
```

## Authentication
Currently, the API endpoints are configured with JWT authentication. For development, you may need to bypass or obtain a valid JWT token.

---

## 1. Student APIs

### Create Student
```http
POST /students
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "password": "SecurePass123!",
  "dob": "2000-01-15",
  "gender": "MALE",
  "address": "123 Main Street",
  "status": "ACTIVE"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "role": "STUDENT",
  "status": "ACTIVE",
  "dob": "2000-01-15",
  "gender": "MALE",
  "address": "123 Main Street",
  "enrollmentDate": "2026-03-03T15:42:40.724+05:30",
  "createdAt": "2026-03-03T15:42:40.724+05:30",
  "updatedAt": "2026-03-03T15:42:40.724+05:30"
}
```

### Get All Students
```http
GET /students
```

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    ...
  }
]
```

### Get Student by ID
```http
GET /students/{id}
```

Example:
```http
GET /students/550e8400-e29b-41d4-a716-446655440000
```

### Update Student (Full)
```http
PUT /students/{id}
Content-Type: application/json

{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "+1234567890",
  "password": "NewPassword123!",
  "dob": "2000-01-15",
  "gender": "MALE",
  "address": "456 Oak Avenue",
  "status": "ACTIVE"
}
```

### Update Student (Partial)
```http
PATCH /students/{id}
Content-Type: application/json

{
  "name": "John Doe Updated",
  "email": "john.updated@example.com"
}
```

### Delete Student
```http
DELETE /students/{id}
```

---

## 2. Faculty APIs

### Create Faculty
```http
POST /faculties
Content-Type: application/json

{
  "name": "Dr. Jane Smith",
  "email": "jane.smith@example.com",
  "phone": "+1987654321",
  "password": "SecurePass123!",
  "position": "Associate Professor",
  "departmentId": "550e8400-e29b-41d4-a716-446655440001",
  "status": "ACTIVE"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "Dr. Jane Smith",
  "email": "jane.smith@example.com",
  "phone": "+1987654321",
  "role": "FACULTY",
  "status": "ACTIVE",
  "position": "Associate Professor",
  "departmentId": "550e8400-e29b-41d4-a716-446655440001",
  "departmentName": "Computer Science",
  "joinDate": "2026-03-03T15:42:40.724+05:30",
  "createdAt": "2026-03-03T15:42:40.724+05:30",
  "updatedAt": "2026-03-03T15:42:40.724+05:30"
}
```

### Get All Faculties
```http
GET /faculties
```

### Get Faculty by ID
```http
GET /faculties/{id}
```

### Get Faculties by Department
```http
GET /faculties/department/{departmentId}
```

### Update Faculty (Full)
```http
PUT /faculties/{id}
Content-Type: application/json

{
  "name": "Dr. Jane Smith Updated",
  "email": "jane.updated@example.com",
  "phone": "+1987654321",
  "password": "NewPassword123!",
  "position": "Professor",
  "departmentId": "550e8400-e29b-41d4-a716-446655440001",
  "status": "ACTIVE"
}
```

### Update Faculty (Partial)
```http
PATCH /faculties/{id}
Content-Type: application/json

{
  "position": "Senior Associate Professor"
}
```

### Delete Faculty
```http
DELETE /faculties/{id}
```

---

## 3. Department APIs

### Create Department
```http
POST /departments
Content-Type: application/json

{
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "headId": null,
  "headName": null,
  "createdAt": "2026-03-03T15:42:40.724+05:30",
  "updatedAt": "2026-03-03T15:42:40.724+05:30"
}
```

### Get All Departments
```http
GET /departments
```

### Get Department by ID
```http
GET /departments/{id}
```

### Get Department Faculties
```http
GET /departments/{id}/faculty
```

### Update Department (Full)
```http
PUT /departments/{id}
Content-Type: application/json

{
  "departmentName": "Computer Science and Engineering",
  "departmentCode": "CSE",
  "contactInfo": "cse@university.edu",
  "status": "ACTIVE",
  "headId": "550e8400-e29b-41d4-a716-446655440002"
}
```

### Update Department (Partial)
```http
PATCH /departments/{id}
Content-Type: application/json

{
  "departmentName": "Computer Science and Engineering"
}
```

### Change Department Head
```http
PATCH /departments/{id}/head
Content-Type: application/json

{
  "headId": "550e8400-e29b-41d4-a716-446655440002"
}
```

### Delete Department
```http
DELETE /departments/{id}
```

---

## Testing with curl

### Example 1: Create Student
```bash
curl -X POST http://localhost:8081/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "password": "SecurePass123!",
    "dob": "2000-01-15",
    "gender": "MALE",
    "address": "123 Main Street",
    "status": "ACTIVE"
  }'
```

### Example 2: Get All Students
```bash
curl -X GET http://localhost:8081/students
```

### Example 3: Get Student by ID
```bash
curl -X GET http://localhost:8081/students/550e8400-e29b-41d4-a716-446655440000
```

### Example 4: Update Student
```bash
curl -X PATCH http://localhost:8081/students/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com"
  }'
```

### Example 5: Delete Student
```bash
curl -X DELETE http://localhost:8081/students/550e8400-e29b-41d4-a716-446655440000
```

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200  | OK - Successful GET/PUT/PATCH |
| 201  | Created - Successful POST |
| 204  | No Content - Successful DELETE |
| 400  | Bad Request - Validation error |
| 404  | Not Found - Resource not found |
| 500  | Internal Server Error |

---

## Validation Rules

### Student
- **name:** Required, non-blank
- **email:** Required, valid email format
- **phone:** Optional, 7-15 digits (format: `^\+?\d{7,15}$`)
- **password:** Required, non-blank
- **dob:** Required, must be in the past
- **gender:** Required (MALE, FEMALE, OTHER)
- **address:** Required, non-blank
- **status:** Required (ACTIVE, INACTIVE, SUSPENDED)

### Faculty
- **name:** Required, non-blank
- **email:** Required, valid email format
- **phone:** Optional, 7-15 digits
- **password:** Required, non-blank
- **position:** Required, non-blank
- **departmentId:** Required, must exist in database
- **status:** Required (ACTIVE, INACTIVE, SUSPENDED)

### Department
- **departmentName:** Required, non-blank
- **departmentCode:** Required, non-blank
- **contactInfo:** Required, non-blank
- **status:** Required (ACTIVE, INACTIVE, SUSPENDED)
- **headId:** Optional, must exist in database (DepartmentHead entity)

---

## Error Response Example

```json
{
  "timestamp": "2026-03-03T15:42:40.724+05:30",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 550e8400-e29b-41d4-a716-446655440099",
  "path": "/students/550e8400-e29b-41d4-a716-446655440099"
}
```

---

## Testing Order

1. **Create Department** first (no dependencies)
2. **Create Faculty** (requires department ID)
3. **Create Student** (no dependencies)
4. **Update/Patch** operations on created entities
5. **Delete** operations (start from student, then faculty, then department)

This ensures referential integrity and prevents constraint violations.

