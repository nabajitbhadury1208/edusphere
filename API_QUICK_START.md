# EduSphere API Testing - Quick Start Guide

## 📋 Summary

This guide provides **step-by-step instructions** to test all APIs for **Student**, **Faculty**, and **Department** management in the EduSphere application.

---

## 🚀 Quick Setup (5 minutes)

### Step 1: Kill Port 8081 (if in use)
```powershell
# Find and kill process on port 8081
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Step 2: Start the Application
```bash
cd C:\Users\2480080\IdeaProjects\edusphere
mvn clean install
mvn spring-boot:run
```

### Step 3: Import Postman Collection
1. Open **Postman**
2. Click **Import**
3. Select: `EduSphere_API_Testing_Collection.postman_collection.json`
4. Create/Select workspace

### Step 4: Create Postman Environment
1. Click **Environments** → **Create New**
2. Name it: `EduSphere Local`
3. Add these variables with empty values:
   - `ADMIN_ACCESS_TOKEN`
   - `ADMIN_REFRESH_TOKEN`
   - `STUDENT_ACCESS_TOKEN`
   - `DEPTHEAD_USER_ID`
   - `DEPT_CS_ID`
   - `DEPT_IT_ID`
   - `FACULTY_ID_1`
   - `STUDENT_ID_1`

---

## 🔐 Testing Sequence

### PHASE 1: Authentication (Get Tokens) ⭐ START HERE

#### 1️⃣ Register Admin
```
Method: POST
URL: http://localhost:8081/api/v1/auth/register
Body:
{
  "name": "John Admin",
  "email": "admin@edusphere.com",
  "password": "Admin@123456",
  "phone": "+919876543210",
  "role": "ADMIN"
}
```
✅ **Copy from response:**
- `accessToken` → Update `ADMIN_ACCESS_TOKEN` in Postman environment
- `refreshToken` → Update `ADMIN_REFRESH_TOKEN`

---

#### 2️⃣ Register Student
```
Method: POST
URL: http://localhost:8081/api/v1/auth/register
Body:
{
  "name": "Ash Student",
  "email": "student@example.com",
  "password": "Password@123",
  "phone": "1234567890",
  "role": "STUDENT"
}
```
✅ **Copy:** `accessToken` → `STUDENT_ACCESS_TOKEN`

---

#### 3️⃣ Register Faculty
```
Method: POST
URL: http://localhost:8081/api/v1/auth/register
Body:
{
  "name": "Dr. Jane Faculty",
  "email": "faculty@example.com",
  "password": "Faculty@123",
  "phone": "9876543210",
  "role": "FACULTY"
}
```
✅ **Copy:** `accessToken` → `FACULTY_ACCESS_TOKEN`

---

#### 4️⃣ Register Department Head
```
Method: POST
URL: http://localhost:8081/api/v1/auth/register
Body:
{
  "name": "Prof. Bob Head",
  "email": "depthead@example.com",
  "password": "DeptHead@123",
  "phone": "8765432109",
  "role": "HEAD"
}
```
✅ **Copy:**
- `id` (from response user object) → `DEPTHEAD_USER_ID`
- `accessToken` → `DEPTHEAD_ACCESS_TOKEN`

---

### PHASE 2: Departments

#### 5️⃣ Create Department (Computer Science)
```
Method: POST
URL: http://localhost:8081/api/v1/departments
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "departmentName": "Computer Science",
  "departmentCode": "CS",
  "contactInfo": "cs@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```
✅ **Copy:** `id` → `DEPT_CS_ID`

---

#### 6️⃣ Create Department (Information Technology)
```
Method: POST
URL: http://localhost:8081/api/v1/departments
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "departmentName": "Information Technology",
  "departmentCode": "IT",
  "contactInfo": "it@university.edu",
  "status": "ACTIVE",
  "headId": null
}
```
✅ **Copy:** `id` → `DEPT_IT_ID`

---

#### 7️⃣ Get All Departments
```
Method: GET
URL: http://localhost:8081/api/v1/departments
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```
✅ **Expected:** Array of 2 departments

---

#### 8️⃣ Get Department by ID
```
Method: GET
URL: http://localhost:8081/api/v1/departments/{{DEPT_CS_ID}}
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```
✅ **Expected:** Single Computer Science department

---

#### 9️⃣ Assign Department Head
```
Method: PATCH
URL: http://localhost:8081/api/v1/departments/{{DEPT_CS_ID}}/head
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "headId": "{{DEPTHEAD_USER_ID}}"
}
```
✅ **Expected:** Department with head assigned

---

### PHASE 3: Faculties

#### 🔟 Create Faculty 1
```
Method: POST
URL: http://localhost:8081/api/v1/faculties
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "name": "Dr. Jane Faculty",
  "email": "faculty1@example.com",
  "phone": "9876543210",
  "password": "Faculty@123",
  "position": "Associate Professor",
  "departmentId": "{{DEPT_CS_ID}}",
  "status": "ACTIVE"
}
```
✅ **Copy:** `id` → `FACULTY_ID_1`

---

#### 1️⃣1️⃣ Create Faculty 2
```
Method: POST
URL: http://localhost:8081/api/v1/faculties
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "name": "Prof. Mike Faculty",
  "email": "faculty2@example.com",
  "phone": "8765432109",
  "password": "Faculty@456",
  "position": "Professor",
  "departmentId": "{{DEPT_CS_ID}}",
  "status": "ACTIVE"
}
```
✅ **Copy:** `id` → `FACULTY_ID_2`

---

#### 1️⃣2️⃣ Get All Faculties
```
Method: GET
URL: http://localhost:8081/api/v1/faculties
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```
✅ **Expected:** Array of 2+ faculties

---

#### 1️⃣3️⃣ Get Faculty by ID
```
Method: GET
URL: http://localhost:8081/api/v1/faculties/{{FACULTY_ID_1}}
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```
✅ **Expected:** Single faculty with department info

---

#### 1️⃣4️⃣ Update Faculty
```
Method: PUT
URL: http://localhost:8081/api/v1/faculties/{{FACULTY_ID_1}}
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
{
  "name": "Dr. Jane Faculty Updated",
  "email": "faculty1-updated@example.com",
  "phone": "9876543211",
  "password": "NewPassword@123",
  "position": "Professor",
  "departmentId": "{{DEPT_CS_ID}}",
  "status": "ACTIVE"
}
```
✅ **Expected:** Updated faculty record

---

#### 1️⃣5️⃣ Partial Update Faculty
```
Method: PATCH
URL: http://localhost:8081/api/v1/faculties/{{FACULTY_ID_1}}
Authorization: Bearer {{FACULTY_ACCESS_TOKEN}}
Body:
{
  "phone": "9876543222"
}
```
✅ **Expected:** Only phone is updated, other fields unchanged

---

### PHASE 4: Students

#### 1️⃣6️⃣ Create Student 1
```
Method: POST
URL: http://localhost:8081/api/v1/students
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
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
✅ **Copy:** `id` → `STUDENT_ID_1`

---

#### 1️⃣7️⃣ Create Student 2
```
Method: POST
URL: http://localhost:8081/api/v1/students
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
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
✅ **Copy:** `id` → `STUDENT_ID_2`

---

#### 1️⃣8️⃣ Get All Students
```
Method: GET
URL: http://localhost:8081/api/v1/students
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
```
✅ **Expected:** Array of 2+ students

---

#### 1️⃣9️⃣ Get Student by ID
```
Method: GET
URL: http://localhost:8081/api/v1/students/{{STUDENT_ID_1}}
Authorization: Bearer {{STUDENT_ACCESS_TOKEN}}
```
✅ **Expected:** Single student record

---

#### 2️⃣0️⃣ Update Student
```
Method: PUT
URL: http://localhost:8081/api/v1/students/{{STUDENT_ID_1}}
Authorization: Bearer {{ADMIN_ACCESS_TOKEN}}
Body:
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
✅ **Expected:** Updated student record

---

#### 2️⃣1️⃣ Partial Update Student
```
Method: PATCH
URL: http://localhost:8081/api/v1/students/{{STUDENT_ID_1}}
Authorization: Bearer {{STUDENT_ACCESS_TOKEN}}
Body:
{
  "phone": "1234567899",
  "address": "999 New Street, City"
}
```
✅ **Expected:** Only phone and address are updated

---

## ⚠️ Important Notes

### Role-Based Access Control
| Role | Can Do |
|------|--------|
| **ADMIN** | Everything |
| **STUDENT** | View own profile, update own profile |
| **FACULTY** | View own profile, update own profile, view department faculties |
| **HEAD** | View department, view faculty, view students in department |

### Enum Values (Copy exactly)
```
Status: ACTIVE, INACTIVE, ARCHIVED
Gender: MALE, FEMALE, OTHER
Role: STUDENT, FACULTY, HEAD, ADMIN, COMPLIANCE, REGULATOR
```

### Error Codes
| Code | Meaning | Solution |
|------|---------|----------|
| 400 | Bad Request | Check JSON syntax, enum values |
| 401 | Unauthorized | Add Authorization header with token |
| 403 | Forbidden | Check user role permissions |
| 404 | Not Found | Verify resource ID exists |
| 500 | Server Error | Check server logs, restart app |

---

## 📊 Test Completion Checklist

### Authentication ✅
- [ ] Register Admin
- [ ] Register Student
- [ ] Register Faculty
- [ ] Register Department Head

### Departments ✅
- [ ] Create CS Department
- [ ] Create IT Department
- [ ] Get All Departments
- [ ] Get Department by ID
- [ ] Update Department
- [ ] Partial Update Department
- [ ] Assign Department Head

### Faculties ✅
- [ ] Create Faculty 1
- [ ] Create Faculty 2
- [ ] Get All Faculties
- [ ] Get Faculty by ID
- [ ] Update Faculty (Full)
- [ ] Update Faculty (Partial)

### Students ✅
- [ ] Create Student 1
- [ ] Create Student 2
- [ ] Get All Students
- [ ] Get Student by ID
- [ ] Update Student (Full)
- [ ] Update Student (Partial)

---

## 📁 Files Created

1. **API_TESTING_GUIDE.md** - Detailed comprehensive guide (this file's complete version)
2. **EduSphere_API_Testing_Collection.postman_collection.json** - Postman collection (import into Postman)
3. **API_QUICK_START.md** - This quick reference guide

---

## 🎯 Expected Outcomes

After completing all tests, you should have:
- ✅ 4 registered users (Admin, Student, Faculty, DeptHead)
- ✅ 2 departments (CS, IT)
- ✅ 2 faculties assigned to CS department
- ✅ 2 students in the system
- ✅ All CRUD operations working
- ✅ Role-based access control verified
- ✅ JWT authentication working

---

## 🆘 Troubleshooting

### Port 8081 already in use
```powershell
# Find process
netstat -ano | findstr :8081
# Kill it
taskkill /PID <PID> /F
```

### Database connection failed
- Check MySQL is running
- Verify credentials in `application-local.yaml`
- Ensure database `edusphere` exists

### JWT token invalid
- Register new user to get new token
- Use Refresh endpoint to get new access token
- Check token format: `Bearer <token>`

### Missing dependencies
```bash
mvn clean install -U
```

---

## 📞 Support

Refer to detailed `API_TESTING_GUIDE.md` for:
- Complete endpoint documentation
- All request/response examples
- Error handling details
- Field validation rules
- Authorization matrix


