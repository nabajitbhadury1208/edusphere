# EduSphere API Testing Guide - Complete Step-by-Step

## Prerequisites
- Postman installed
- Server running on `http://localhost:8081`
- Database properly configured
- All required fields populated

---

## PHASE 0: INITIAL SETUP

### Step 0.1: Kill Port 8081 (If already in use)
```powershell
# Find process using port 8081
netstat -ano | findstr :8081

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Step 0.2: Clean and Build Project
```bash
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started EduSphere in X.XXX seconds
Tomcat initialized with port 8081 (http)
```

---

## PHASE 1: AUTHENTICATION ENDPOINTS

### 1.1 Register New User (Admin Role)
**Endpoint:** `POST /api/v1/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Admin",
  "email": "admin@edusphere.com",
  "password": "Admin@123456",
  "phone": "+919876543210",
  "role": "ADMIN"
}
```

**Valid Role Values:**
- `HEAD` - Department Head
- `REGULATOR` - External Regulator
- `FACULTY` - Faculty Member
- `STUDENT` - Student
- `ADMIN` - System Administrator
- `COMPLIANCE` - Compliance Officer

**Expected Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Copy these tokens for later use:**
- `ADMIN_ACCESS_TOKEN` = accessToken value
- `ADMIN_REFRESH_TOKEN` = refreshToken value

---

### 1.2 Register Student User
**Endpoint:** `POST /api/v1/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Ash Student",
  "email": "student@example.com",
  "password": "Password@123",
  "phone": "1234567890",
  "role": "STUDENT"
}
```

**Expected Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Copy these tokens:**
- `STUDENT_ACCESS_TOKEN` = accessToken value
- `STUDENT_REFRESH_TOKEN` = refreshToken value

---

### 1.3 Register Faculty User
**Endpoint:** `POST /api/v1/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Dr. Jane Faculty",
  "email": "faculty@example.com",
  "password": "Faculty@123",
  "phone": "9876543210",
  "role": "FACULTY"
}
```

**Expected Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Copy these tokens:**
- `FACULTY_ACCESS_TOKEN` = accessToken value
- `FACULTY_REFRESH_TOKEN` = refreshToken value

---

### 1.4 Register Department Head User
**Endpoint:** `POST /api/v1/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Prof. Bob Head",
  "email": "depthead@example.com",
  "password": "DeptHead@123",
  "phone": "8765432109",
  "role": "HEAD"
}
```

**Expected Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Copy these tokens:**
- `DEPTHEAD_ACCESS_TOKEN` = accessToken value
- `DEPTHEAD_REFRESH_TOKEN` = refreshToken value

---

### 1.5 Login Endpoint (Alternative to Register)
**Endpoint:** `POST /api/v1/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "admin@edusphere.com",
  "password": "Admin@123456"
}
```

**Expected Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 1.6 Refresh Token
**Endpoint:** `POST /api/v1/auth/refresh`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
}
```

**Expected Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## PHASE 2: DEPARTMENT ENDPOINTS

### 2.1 Create Department (Admin Only)
**Endpoint:** `POST /api/v1/departments`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```

**Status Values:** `ACTIVE`, `INACTIVE`, `ARCHIVED`

**Expected Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "head": null,
  "createdAt": "2026-03-03T15:30:00Z",
  "updatedAt": "2026-03-03T15:30:00Z"
}
```

**Save Department ID:**
- `DEPT_CS_ID` = id from response

---

### 2.2 Create Second Department
**Endpoint:** `POST /api/v1/departments`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "departmentName": "Information Technology",
  "departmentCode": "IT",
  "contactInfo": "it@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```

**Expected Response (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  ...
}
```

**Save Department ID:**
- `DEPT_IT_ID` = id from response

---

### 2.3 Get All Departments
**Endpoint:** `GET /api/v1/departments`

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "departmentName": "Computer Science",
    "departmentCode": "CS",
    "contactInfo": "cs@university.edu",
    "status": "ACTIVE",
    "head": null,
    "createdAt": "2026-03-03T15:30:00Z",
    "updatedAt": "2026-03-03T15:30:00Z"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "departmentName": "Information Technology",
    "departmentCode": "IT",
    "contactInfo": "it@university.edu",
    "status": "ACTIVE",
    "head": null,
    "createdAt": "2026-03-03T15:31:00Z",
    "updatedAt": "2026-03-03T15:31:00Z"
  }
]
```

---

### 2.4 Get Department by ID
**Endpoint:** `GET /api/v1/departments/{departmentId}`

**Replace:** `{departmentId}` with `DEPT_CS_ID`

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "head": null,
  "createdAt": "2026-03-03T15:30:00Z",
  "updatedAt": "2026-03-03T15:30:00Z"
}
```

---

### 2.5 Update Department (Full Update)
**Endpoint:** `PUT /api/v1/departments/{departmentId}`

**Replace:** `{departmentId}` with `DEPT_CS_ID`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "departmentName": "Computer Science & Engineering",
  "departmentCode": "CSE",
  "contactInfo": "cse@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Computer Science & Engineering",
  "departmentCode": "CSE",
  "contactInfo": "cse@university.edu",
  "status": "ACTIVE",
  "head": null,
  "createdAt": "2026-03-03T15:30:00Z",
  "updatedAt": "2026-03-03T15:35:00Z"
}
```

---

### 2.6 Partially Update Department
**Endpoint:** `PATCH /api/v1/departments/{departmentId}`

**Replace:** `{departmentId}` with `DEPT_CS_ID`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body (Only include fields to update):**
```json
{
  "contactInfo": "cse-new@university.edu",
  "status": "INACTIVE"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Computer Science & Engineering",
  "departmentCode": "CSE",
  "contactInfo": "cse-new@university.edu",
  "status": "INACTIVE",
  "head": null,
  "createdAt": "2026-03-03T15:30:00Z",
  "updatedAt": "2026-03-03T15:40:00Z"
}
```

---

### 2.7 Assign Department Head
**Endpoint:** `PATCH /api/v1/departments/{departmentId}/head`

**Replace:** `{departmentId}` with `DEPT_CS_ID`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "headId": "DEPTHEAD_USER_ID"
}
```

**Note:** You need to use the user ID from the department head registration response.

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Computer Science & Engineering",
  "departmentCode": "CSE",
  "contactInfo": "cse-new@university.edu",
  "status": "INACTIVE",
  "head": {
    "id": "DEPTHEAD_USER_ID",
    "name": "Prof. Bob Head",
    "email": "depthead@example.com",
    "role": "HEAD"
  },
  "createdAt": "2026-03-03T15:30:00Z",
  "updatedAt": "2026-03-03T15:45:00Z"
}
```

---

### 2.8 Get All Faculty in Department
**Endpoint:** `GET /api/v1/departments/{departmentId}/faculty`

**Replace:** `{departmentId}` with `DEPT_CS_ID`

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (200 OK):**
```json
[
  {
    "id": "FACULTY_USER_ID_1",
    "name": "Dr. Jane Faculty",
    "email": "faculty@example.com",
    "position": "Associate Professor",
    "status": "ACTIVE",
    "joinDate": "2026-03-03T15:30:00Z"
  }
]
```

---

### 2.9 Delete Department
**Endpoint:** `DELETE /api/v1/departments/{departmentId}`

**Replace:** `{departmentId}` with a department ID you want to delete

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (204 No Content):**
(Empty response)

---

## PHASE 3: FACULTY ENDPOINTS

### 3.1 Create Faculty (Admin Only)
**Endpoint:** `POST /api/v1/faculties`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Dr. Jane Faculty",
  "email": "faculty1@example.com",
  "phone": "9876543210",
  "password": "Faculty@123",
  "position": "Associate Professor",
  "departmentId": "DEPT_CS_ID",
  "status": "ACTIVE"
}
```

**Expected Response (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Dr. Jane Faculty",
  "email": "faculty1@example.com",
  "phone": "9876543210",
  "position": "Associate Professor",
  "role": "FACULTY",
  "department": {
    "id": "DEPT_CS_ID",
    "departmentName": "Computer Science & Engineering",
    "departmentCode": "CSE"
  },
  "status": "ACTIVE",
  "joinDate": "2026-03-03T15:50:00Z",
  "createdAt": "2026-03-03T15:50:00Z",
  "updatedAt": "2026-03-03T15:50:00Z"
}
```

**Save Faculty ID:**
- `FACULTY_ID_1` = id from response

---

### 3.2 Create Second Faculty
**Endpoint:** `POST /api/v1/faculties`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Prof. Mike Faculty",
  "email": "faculty2@example.com",
  "phone": "8765432109",
  "password": "Faculty@456",
  "position": "Professor",
  "departmentId": "DEPT_CS_ID",
  "status": "ACTIVE"
}
```

**Expected Response (201 Created):**
```json
{
  "id": "880e8400-e29b-41d4-a716-446655440003",
  ...
}
```

**Save Faculty ID:**
- `FACULTY_ID_2` = id from response

---

### 3.3 Get All Faculties
**Endpoint:** `GET /api/v1/faculties`

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (200 OK):**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Dr. Jane Faculty",
    "email": "faculty1@example.com",
    "phone": "9876543210",
    "position": "Associate Professor",
    "role": "FACULTY",
    "department": {
      "id": "DEPT_CS_ID",
      "departmentName": "Computer Science & Engineering",
      "departmentCode": "CSE"
    },
    "status": "ACTIVE",
    "joinDate": "2026-03-03T15:50:00Z",
    "createdAt": "2026-03-03T15:50:00Z",
    "updatedAt": "2026-03-03T15:50:00Z"
  },
  {
    "id": "880e8400-e29b-41d4-a716-446655440003",
    ...
  }
]
```

---

### 3.4 Get Faculty by ID
**Endpoint:** `GET /api/v1/faculties/{facultyId}`

**Replace:** `{facultyId}` with `FACULTY_ID_1`

**Headers:**
```
Authorization: Bearer FACULTY_ACCESS_TOKEN
```
(Or any admin/depthead token)

**Expected Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Dr. Jane Faculty",
  "email": "faculty1@example.com",
  "phone": "9876543210",
  "position": "Associate Professor",
  "role": "FACULTY",
  "department": {
    "id": "DEPT_CS_ID",
    "departmentName": "Computer Science & Engineering",
    "departmentCode": "CSE"
  },
  "status": "ACTIVE",
  "joinDate": "2026-03-03T15:50:00Z",
  "createdAt": "2026-03-03T15:50:00Z",
  "updatedAt": "2026-03-03T15:50:00Z"
}
```

---

### 3.5 Update Faculty (Full Update)
**Endpoint:** `PUT /api/v1/faculties/{facultyId}`

**Replace:** `{facultyId}` with `FACULTY_ID_1`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Dr. Jane Faculty Updated",
  "email": "faculty1-updated@example.com",
  "phone": "9876543211",
  "password": "NewPassword@123",
  "position": "Professor",
  "departmentId": "DEPT_CS_ID",
  "status": "ACTIVE"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Dr. Jane Faculty Updated",
  "email": "faculty1-updated@example.com",
  "phone": "9876543211",
  "position": "Professor",
  "role": "FACULTY",
  "department": {
    "id": "DEPT_CS_ID",
    "departmentName": "Computer Science & Engineering",
    "departmentCode": "CSE"
  },
  "status": "ACTIVE",
  "joinDate": "2026-03-03T15:50:00Z",
  "createdAt": "2026-03-03T15:50:00Z",
  "updatedAt": "2026-03-03T16:00:00Z"
}
```

---

### 3.6 Partially Update Faculty
**Endpoint:** `PATCH /api/v1/faculties/{facultyId}`

**Replace:** `{facultyId}` with `FACULTY_ID_1`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer FACULTY_ACCESS_TOKEN
```
(Faculty can update own profile)

**Request Body (Only include fields to update):**
```json
{
  "phone": "9876543222",
  "status": "INACTIVE"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Dr. Jane Faculty Updated",
  "email": "faculty1-updated@example.com",
  "phone": "9876543222",
  "position": "Professor",
  "role": "FACULTY",
  "department": {
    "id": "DEPT_CS_ID",
    "departmentName": "Computer Science & Engineering",
    "departmentCode": "CSE"
  },
  "status": "INACTIVE",
  "joinDate": "2026-03-03T15:50:00Z",
  "createdAt": "2026-03-03T15:50:00Z",
  "updatedAt": "2026-03-03T16:10:00Z"
}
```

---

### 3.7 Delete Faculty
**Endpoint:** `DELETE /api/v1/faculties/{facultyId}`

**Replace:** `{facultyId}` with a faculty ID you want to delete

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (204 No Content):**
(Empty response)

---

## PHASE 4: STUDENT ENDPOINTS

### 4.1 Create Student (Admin Only)
**Endpoint:** `POST /api/v1/students`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Ash Student",
  "email": "student1@example.com",
  "phone": "1234567890",
  "password": "Student@123",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "123 Main Street, City",
  "status": "ACTIVE"
}
```

**Gender Values:** `MALE`, `FEMALE`, `OTHER`

**Expected Response (201 Created):**
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440004",
  "name": "Ash Student",
  "email": "student1@example.com",
  "phone": "1234567890",
  "role": "STUDENT",
  "status": "ACTIVE",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "123 Main Street, City",
  "enrollmentDate": "2026-03-03T16:15:00Z",
  "createdAt": "2026-03-03T16:15:00Z",
  "updatedAt": "2026-03-03T16:15:00Z"
}
```

**Save Student ID:**
- `STUDENT_ID_1` = id from response

---

### 4.2 Create Second Student
**Endpoint:** `POST /api/v1/students`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Sarah Student",
  "email": "student2@example.com",
  "phone": "9876543212",
  "password": "Student@456",
  "dob": "2006-05-20",
  "gender": "FEMALE",
  "address": "456 Oak Avenue, City",
  "status": "ACTIVE"
}
```

**Expected Response (201 Created):**
```json
{
  "id": "aa0e8400-e29b-41d4-a716-446655440005",
  ...
}
```

**Save Student ID:**
- `STUDENT_ID_2` = id from response

---

### 4.3 Get All Students
**Endpoint:** `GET /api/v1/students`

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (200 OK):**
```json
[
  {
    "id": "990e8400-e29b-41d4-a716-446655440004",
    "name": "Ash Student",
    "email": "student1@example.com",
    "phone": "1234567890",
    "role": "STUDENT",
    "status": "ACTIVE",
    "dob": "2005-01-15",
    "gender": "MALE",
    "address": "123 Main Street, City",
    "enrollmentDate": "2026-03-03T16:15:00Z",
    "createdAt": "2026-03-03T16:15:00Z",
    "updatedAt": "2026-03-03T16:15:00Z"
  },
  {
    "id": "aa0e8400-e29b-41d4-a716-446655440005",
    ...
  }
]
```

---

### 4.4 Get Student by ID
**Endpoint:** `GET /api/v1/students/{studentId}`

**Replace:** `{studentId}` with `STUDENT_ID_1`

**Headers:**
```
Authorization: Bearer STUDENT_ACCESS_TOKEN
```
(Or admin/depthead token)

**Expected Response (200 OK):**
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440004",
  "name": "Ash Student",
  "email": "student1@example.com",
  "phone": "1234567890",
  "role": "STUDENT",
  "status": "ACTIVE",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "123 Main Street, City",
  "enrollmentDate": "2026-03-03T16:15:00Z",
  "createdAt": "2026-03-03T16:15:00Z",
  "updatedAt": "2026-03-03T16:15:00Z"
}
```

---

### 4.5 Update Student (Full Update)
**Endpoint:** `PUT /api/v1/students/{studentId}`

**Replace:** `{studentId}` with `STUDENT_ID_1`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Request Body:**
```json
{
  "name": "Ash Student Updated",
  "email": "student1-updated@example.com",
  "phone": "1234567891",
  "password": "NewStudent@123",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "789 Pine Road, City",
  "status": "ACTIVE"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440004",
  "name": "Ash Student Updated",
  "email": "student1-updated@example.com",
  "phone": "1234567891",
  "role": "STUDENT",
  "status": "ACTIVE",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "789 Pine Road, City",
  "enrollmentDate": "2026-03-03T16:15:00Z",
  "createdAt": "2026-03-03T16:15:00Z",
  "updatedAt": "2026-03-03T16:30:00Z"
}
```

---

### 4.6 Partially Update Student (Student Can Update Own)
**Endpoint:** `PATCH /api/v1/students/{studentId}`

**Replace:** `{studentId}` with `STUDENT_ID_1`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer STUDENT_ACCESS_TOKEN
```

**Request Body (Only include fields to update):**
```json
{
  "phone": "1234567899",
  "address": "999 New Street, City"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440004",
  "name": "Ash Student Updated",
  "email": "student1-updated@example.com",
  "phone": "1234567899",
  "role": "STUDENT",
  "status": "ACTIVE",
  "dob": "2005-01-15",
  "gender": "MALE",
  "address": "999 New Street, City",
  "enrollmentDate": "2026-03-03T16:15:00Z",
  "createdAt": "2026-03-03T16:15:00Z",
  "updatedAt": "2026-03-03T16:40:00Z"
}
```

---

### 4.7 Delete Student
**Endpoint:** `DELETE /api/v1/students/{studentId}`

**Replace:** `{studentId}` with a student ID you want to delete

**Headers:**
```
Authorization: Bearer ADMIN_ACCESS_TOKEN
```

**Expected Response (204 No Content):**
(Empty response)

---

## PHASE 5: POSTMAN ENVIRONMENT SETUP

To make testing easier, create a Postman Environment with these variables:

```
BASE_URL = http://localhost:8081
API_VERSION = api/v1

ADMIN_ACCESS_TOKEN = (token from 1.1)
ADMIN_REFRESH_TOKEN = (token from 1.1)

STUDENT_ACCESS_TOKEN = (token from 1.2)
STUDENT_REFRESH_TOKEN = (token from 1.2)

FACULTY_ACCESS_TOKEN = (token from 1.3)
FACULTY_REFRESH_TOKEN = (token from 1.3)

DEPTHEAD_ACCESS_TOKEN = (token from 1.4)
DEPTHEAD_REFRESH_TOKEN = (token from 1.4)

DEPTHEAD_USER_ID = (user id from 1.4)
DEPT_CS_ID = (id from 2.1)
DEPT_IT_ID = (id from 2.2)
FACULTY_ID_1 = (id from 3.1)
FACULTY_ID_2 = (id from 3.2)
STUDENT_ID_1 = (id from 4.1)
STUDENT_ID_2 = (id from 4.2)
```

Then use variables in URLs:
```
{{BASE_URL}}/{{API_VERSION}}/departments
```

And in headers:
```
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```

---

## PHASE 6: ERROR HANDLING & TROUBLESHOOTING

### Common Errors and Solutions

#### 400 Bad Request
**Cause:** Invalid request body, missing required fields, or invalid enum values
**Solution:** 
- Verify all required fields are present
- Check enum values match exactly (case-sensitive)
- Validate email format and password requirements

#### 401 Unauthorized
**Cause:** Missing or invalid Authorization header
**Solution:**
- Ensure token is included in Authorization header
- Format: `Bearer YOUR_TOKEN_HERE`
- Refresh token if it has expired

#### 403 Forbidden
**Cause:** User lacks permission for this operation
**Solution:**
- Check role-based access control
- Admin can perform most operations
- Students can only update own profile
- Faculty can only update own profile

#### 404 Not Found
**Cause:** Resource ID doesn't exist
**Solution:**
- Verify ID exists by fetching all records first
- Copy ID exactly from previous response

#### 500 Internal Server Error
**Cause:** Server error
**Solution:**
- Check server logs
- Verify database connection
- Restart application

---

## PHASE 7: TESTING CHECKLIST

### Authentication Tests
- [ ] 1.1 Register Admin - Success
- [ ] 1.2 Register Student - Success
- [ ] 1.3 Register Faculty - Success
- [ ] 1.4 Register Dept Head - Success
- [ ] 1.5 Login - Success
- [ ] 1.6 Refresh Token - Success

### Department Tests
- [ ] 2.1 Create Department CS - Success
- [ ] 2.2 Create Department IT - Success
- [ ] 2.3 Get All Departments - Success
- [ ] 2.4 Get Department by ID - Success
- [ ] 2.5 Update Department - Success
- [ ] 2.6 Partial Update - Success
- [ ] 2.7 Assign Department Head - Success
- [ ] 2.8 Get Faculty in Department - Success
- [ ] 2.9 Delete Department - Success

### Faculty Tests
- [ ] 3.1 Create Faculty 1 - Success
- [ ] 3.2 Create Faculty 2 - Success
- [ ] 3.3 Get All Faculties - Success
- [ ] 3.4 Get Faculty by ID - Success
- [ ] 3.5 Update Faculty - Success
- [ ] 3.6 Partial Update Faculty - Success
- [ ] 3.7 Delete Faculty - Success

### Student Tests
- [ ] 4.1 Create Student 1 - Success
- [ ] 4.2 Create Student 2 - Success
- [ ] 4.3 Get All Students - Success
- [ ] 4.4 Get Student by ID - Success
- [ ] 4.5 Update Student - Success
- [ ] 4.6 Partial Update Student - Success
- [ ] 4.7 Delete Student - Success

### Authorization Tests
- [ ] Student cannot create department
- [ ] Faculty cannot delete student
- [ ] DeptHead can view department faculties
- [ ] Admin can perform all operations

---

## PHASE 8: ADDITIONAL NOTES

### Field Validation Rules

**Email:**
- Must be valid email format
- Example: `user@example.com`

**Phone:**
- Must be 7-15 digits
- Can include `+` at start
- Examples: `1234567890`, `+919876543210`

**Password:**
- Minimum 8 characters recommended
- Use mix of uppercase, lowercase, numbers

**Dates:**
- Format: `YYYY-MM-DD`
- Example: `2005-01-15`

**Status Enum:**
- `ACTIVE` - Entity is active
- `INACTIVE` - Entity is inactive
- `ARCHIVED` - Entity is archived

**Gender Enum:**
- `MALE`
- `FEMALE`
- `OTHER`

**Role Enum:**
- `STUDENT` - Student user
- `FACULTY` - Faculty member
- `HEAD` - Department head
- `ADMIN` - Administrator
- `COMPLIANCE` - Compliance officer
- `REGULATOR` - External regulator

### JWT Token Information
- Access tokens expire after 15 minutes (typical)
- Refresh tokens can be used to get new access tokens
- Tokens contain user ID, name, and role information
- Always include "Bearer " prefix before token in headers

---

## PHASE 9: QUICK REFERENCE URLS

```
POST   http://localhost:8081/api/v1/auth/register
POST   http://localhost:8081/api/v1/auth/login
POST   http://localhost:8081/api/v1/auth/refresh

POST   http://localhost:8081/api/v1/departments
GET    http://localhost:8081/api/v1/departments
GET    http://localhost:8081/api/v1/departments/{id}
PUT    http://localhost:8081/api/v1/departments/{id}
PATCH  http://localhost:8081/api/v1/departments/{id}
PATCH  http://localhost:8081/api/v1/departments/{id}/head
GET    http://localhost:8081/api/v1/departments/{id}/faculty
DELETE http://localhost:8081/api/v1/departments/{id}

POST   http://localhost:8081/api/v1/faculties
GET    http://localhost:8081/api/v1/faculties
GET    http://localhost:8081/api/v1/faculties/{id}
PUT    http://localhost:8081/api/v1/faculties/{id}
PATCH  http://localhost:8081/api/v1/faculties/{id}
DELETE http://localhost:8081/api/v1/faculties/{id}

POST   http://localhost:8081/api/v1/students
GET    http://localhost:8081/api/v1/students
GET    http://localhost:8081/api/v1/students/{id}
PUT    http://localhost:8081/api/v1/students/{id}
PATCH  http://localhost:8081/api/v1/students/{id}
DELETE http://localhost:8081/api/v1/students/{id}
```

---

## Success Indicators

Your API is working correctly when:
- ✅ All registration endpoints return 201 with valid tokens
- ✅ All authentication endpoints work properly
- ✅ Department CRUD operations complete successfully
- ✅ Faculty CRUD operations complete successfully
- ✅ Student CRUD operations complete successfully
- ✅ Authorization checks prevent unauthorized access
- ✅ All responses include proper timestamps
- ✅ IDs are properly returned in responses


