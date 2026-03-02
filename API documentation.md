# EduSphere — API Documentation

## Role Legend

| Role               | Abbreviation | Primary Responsibility                                    |
|--------------------|:------------:|---------------------------------------------------------|
| Student            | `Student`    | Learning & submission — personal records & enrolled courses only |
| Faculty Member     | `Faculty`    | Teaching & research — assigned courses, students, and research projects |
| Department Head    | `DeptHead`   | Academic oversight — all faculty and students within their department |
| Administrator      | `Admin`      | System operations — global config, user management, system-wide logs |
| Compliance Officer | `Compliance` | Internal auditing — academic records, audit trails, and policy adherence |
| Regulator          | `Regulator`  | External monitoring — high-level reports, accreditation metrics, final audits |

---

## Access Control Matrix

| Module / Entity    | Student  | Faculty  | DeptHead | Admin | Compliance | Regulator |
|--------------------|:--------:|:--------:|:--------:|:-----:|:----------:|:---------:|
| Identity (Users)   | Profile  | Profile  | View Dept| Full  | View All   | View All  |
| Enrollment         | Write    | Read     | Update   | Full  | Audit      | Read      |
| Curriculum         | Read     | Update   | Full     | Read  | Audit      | Read      |
| Workload           | None     | Read     | Full     | Read  | Audit      | Read      |
| Grades             | Read     | Full     | Read     | Read  | Audit      | Read      |
| Thesis / Research  | Write    | Full     | Read     | Read  | Read       | Read      |
| Audit Logs         | None     | None     | None     | Read  | Full       | Read      |
| System Config      | None     | None     | None     | Full  | None       | None      |

---

## Auth API

| Sl No | Method   | Endpoint                    | Description                  | Role                         |
|:------|:---------|:----------------------------|:-----------------------------|:-----------------------------|
| 1     | `POST`   | `/auth/register`            | Register the user            | All                          |
| 2     | `POST`   | `/auth/login`               | Log the user in              | All                          |
| 3     | `POST`   | `/auth/logout`              | Log the user out             | All                          |
| 4     | `POST`   | `/auth/refresh`             | Refresh access token         | All                          |
| 5     | `PATCH`  | `/auth/change-password`     | Change the user password     | All                          |

---

## User API

| Sl No | Method   | Endpoint                    | Description                        | Role                         |
|:------|:---------|:----------------------------|:-----------------------------------|:-----------------------------|
| 1     | `POST`   | `/users`                    | Create a new user                  | Admin                        |
| 2     | `GET`    | `/users`                    | Get all users                      | Admin                        |
| 3     | `GET`    | `/users/{id}`               | Get a user by id                   | Admin                        |
| 4     | `PUT`    | `/users/{id}`               | Fully update a user by id          | Admin                        |
| 5     | `PATCH`  | `/users/{id}`               | Partially update a user by id      | Admin                        |
| 6     | `PATCH`  | `/users/{id}/status`        | Activate / Deactivate a user       | Admin                        |
| 7     | `DELETE` | `/users/{id}`               | Delete a user by id                | Admin                        |

---

## Student API

| Sl No | Method   | Endpoint                    | Description                        | Role                                    |
|:------|:---------|:----------------------------|:-----------------------------------|:----------------------------------------|
| 1     | `POST`   | `/students`                 | Create a new student               | Admin                                   |
| 2     | `GET`    | `/students`                 | Get all students                   | Admin, DeptHead                         |
| 3     | `GET`    | `/students/{id}`            | Get a student by id                | Admin, DeptHead, Student (own)          |
| 4     | `PUT`    | `/students/{id}`            | Fully update a student by id       | Admin                                   |
| 5     | `PATCH`  | `/students/{id}`            | Partially update a student by id   | Admin, Student (own profile)            |
| 6     | `DELETE` | `/students/{id}`            | Delete a student by id             | Admin                                   |

---

## Faculty API

| Sl No | Method   | Endpoint                    | Description                        | Role                                    |
|:------|:---------|:----------------------------|:-----------------------------------|:----------------------------------------|
| 1     | `POST`   | `/faculties`                | Create a new faculty               | Admin                                   |
| 2     | `GET`    | `/faculties`                | Get all faculties                  | Admin, DeptHead                         |
| 3     | `GET`    | `/faculties/{id}`           | Get a faculty by id                | Admin, DeptHead, Faculty (own)          |
| 4     | `PUT`    | `/faculties/{id}`           | Fully update a faculty by id       | Admin                                   |
| 5     | `PATCH`  | `/faculties/{id}`           | Partially update a faculty by id   | Admin, Faculty (own profile)            |
| 6     | `DELETE` | `/faculties/{id}`           | Delete a faculty by id             | Admin                                   |

---

## Department API

| Sl No | Method   | Endpoint                            | Description                                    | Role                         |
|:------|:---------|:------------------------------------|:-----------------------------------------------|:-----------------------------|
| 1     | `POST`   | `/departments`                      | Create a new department                        | Admin                        |
| 2     | `GET`    | `/departments`                      | Get all departments                            | Admin, DeptHead              |
| 3     | `GET`    | `/departments/{id}`                 | Get a department by id                         | Admin, DeptHead              |
| 4     | `PUT`    | `/departments/{id}`                 | Fully update a department by id                | Admin                        |
| 5     | `PATCH`  | `/departments/{id}`                 | Partially update a department by id            | Admin                        |
| 6     | `DELETE` | `/departments/{id}`                 | Delete a department by id                      | Admin                        |
| 7     | `PATCH`  | `/departments/{id}/head`            | Change the department head                     | Admin                        |
| 8     | `GET`    | `/departments/{id}/faculty`         | Get all faculties of a department              | Admin, DeptHead              |
| 9     | `GET`    | `/departments/{id}/courses`         | Get all courses of a department                | Admin, DeptHead, Faculty     |

---

## Courses API

| Sl No | Method   | Endpoint                    | Description                        | Role                                        |
|:------|:---------|:----------------------------|:-----------------------------------|:--------------------------------------------|
| 1     | `POST`   | `/courses`                  | Create a new course                | Admin                                       |
| 2     | `GET`    | `/courses`                  | Get all courses                    | Admin, DeptHead, Faculty, Student           |
| 3     | `GET`    | `/courses/{id}`             | Get a course by id                 | Admin, DeptHead, Faculty, Student           |
| 4     | `PUT`    | `/courses/{id}`             | Fully update a course by id        | Admin                                       |
| 5     | `PATCH`  | `/courses/{id}`             | Partially update a course by id    | Admin                                       |
| 6     | `PATCH`  | `/courses/{id}/status`      | Activate / Deactivate a course     | Admin                                       |
| 7     | `DELETE` | `/courses/{id}`             | Delete a course by id              | Admin                                       |

---

## Curriculum API

| Sl No | Method   | Endpoint                    | Description                        | Role                                              |
|:------|:---------|:----------------------------|:-----------------------------------|:--------------------------------------------------|
| 1     | `POST`   | `/curriculum`               | Create a new curriculum            | Admin, DeptHead                                   |
| 2     | `GET`    | `/curriculum`               | Get all curriculums                | Admin, DeptHead, Faculty, Student, Compliance     |
| 3     | `GET`    | `/curriculum/{id}`          | Get a curriculum by id             | Admin, DeptHead, Faculty, Student, Compliance     |
| 4     | `PUT`    | `/curriculum/{id}`          | Fully update a curriculum by id    | Admin, DeptHead                                   |
| 5     | `PATCH`  | `/curriculum/{id}`          | Partially update a curriculum by id| Admin, DeptHead, Faculty                          |
| 6     | `DELETE` | `/curriculum/{id}`          | Delete a curriculum by id          | Admin                                             |

---

## Exams API

| Sl No | Method   | Endpoint                            | Description                        | Role                                        |
|:------|:---------|:------------------------------------|:-----------------------------------|:--------------------------------------------|
| 1     | `POST`   | `/exams`                            | Create a new exam                  | Admin, Faculty                              |
| 2     | `GET`    | `/exams`                            | Get all exams                      | Admin, Faculty, DeptHead                    |
| 3     | `GET`    | `/exams/{id}`                       | Get an exam by id                  | Admin, Faculty, Student                     |
| 4     | `PUT`    | `/exams/{id}`                       | Fully update an exam by id         | Admin, Faculty                              |
| 5     | `PATCH`  | `/exams/{id}`                       | Partially update an exam by id     | Admin, Faculty                              |
| 6     | `PATCH`  | `/exams/{id}/status`                | Activate / Deactivate an exam      | Admin, Faculty                              |
| 7     | `DELETE` | `/exams/{id}`                       | Delete an exam by id               | Admin                                       |
| 8     | `GET`    | `/exams/course/{courseId}`          | Get all exams for a course         | Admin, Faculty, Student                     |

---

## Grades API

| Sl No | Method   | Endpoint                            | Description                              | Role                                              |
|:------|:---------|:------------------------------------|:-----------------------------------------|:--------------------------------------------------|
| 1     | `POST`   | `/grades`                           | Submit a grade                           | Admin, Faculty                                    |
| 2     | `GET`    | `/grades`                           | Get all grades                           | Admin, Compliance                                 |
| 3     | `GET`    | `/grades/{id}`                      | Get a grade by id                        | Admin, Faculty, Compliance                        |
| 4     | `GET`    | `/grades/students/{studentId}`      | Get all grades of a student              | Admin, Faculty, DeptHead, Student (own), Compliance |
| 5     | `GET`    | `/grades/exam/{examId}`             | Get all grades for an exam               | Admin, Faculty, Compliance                        |
| 6     | `PATCH`  | `/grades/{id}`                      | Update a grade by id                     | Admin, Faculty                                    |
| 7     | `DELETE` | `/grades/{id}`                      | Delete a grade by id                     | Admin                                             |

---

## Workload API

| Sl No | Method   | Endpoint                                    | Description                              | Role                              |
|:------|:---------|:--------------------------------------------|:-----------------------------------------|:----------------------------------|
| 1     | `POST`   | `/workload`                                 | Create a new workload (assign to faculty)| Admin, DeptHead                   |
| 2     | `GET`    | `/workload`                                 | Get all workloads                        | Admin, DeptHead, Compliance       |
| 3     | `GET`    | `/workload/{id}`                            | Get a workload by id                     | Admin, DeptHead, Faculty (own)    |
| 4     | `GET`    | `/workload/faculty/{facultyId}`             | Get workloads for a faculty              | Admin, DeptHead, Faculty (own), Compliance |
| 5     | `PATCH`  | `/workload/{id}`                            | Update a workload by id                  | Admin, DeptHead                   |
| 6     | `DELETE` | `/workload/{id}`                            | Delete a workload by id                  | Admin                             |

---

## Thesis API

| Sl No | Method   | Endpoint                                    | Description                              | Role                                         |
|:------|:---------|:--------------------------------------------|:-----------------------------------------|:---------------------------------------------|
| 1     | `POST`   | `/thesis`                                   | Create a new thesis                      | Admin, Faculty, Student                      |
| 2     | `GET`    | `/thesis/{id}`                              | Get a thesis by id                       | Admin, Faculty, Student (own), Compliance    |
| 3     | `GET`    | `/thesis/student/{studentId}`               | Get all theses for a student             | Admin, Faculty, DeptHead, Student (own), Compliance |
| 4     | `GET`    | `/thesis/supervisor/{facultyId}`            | Get all theses supervised by a faculty   | Admin, DeptHead, Faculty (own), Compliance   |
| 5     | `PUT`    | `/thesis/{id}`                              | Fully update a thesis by id              | Admin, Faculty                               |
| 6     | `PATCH`  | `/thesis/{id}`                              | Partially update a thesis by id          | Admin, Faculty, Student (own)                |
| 7     | `DELETE` | `/thesis/{id}`                              | Delete a thesis by id                    | Admin                                        |

---

## Student Documents API

| Sl No | Method   | Endpoint                                          | Description                               | Role                                   |
|:------|:---------|:--------------------------------------------------|:------------------------------------------|:---------------------------------------|
| 1     | `POST`   | `/student-documents`                              | Upload a new student document             | Admin, Student (own)                   |
| 2     | `GET`    | `/student-documents/{id}`                         | Get a student document by id              | Admin, Student (own), Compliance       |
| 3     | `GET`    | `/student-documents/student/{studentId}`          | Get all documents for a student           | Admin, DeptHead, Student (own), Compliance |
| 4     | `PATCH`  | `/student-documents/{id}/verify`                  | Verify a student document                 | Admin, Compliance                      |
| 5     | `DELETE` | `/student-documents/{id}`                         | Delete a student document by id           | Admin                                  |

---

## Research Projects API

| Sl No | Method   | Endpoint                                                    | Description                              | Role                                              |
|:------|:---------|:------------------------------------------------------------|:-----------------------------------------|:--------------------------------------------------|
| 1     | `POST`   | `/research-projects`                                        | Create a new research project            | Admin, Faculty                                    |
| 2     | `GET`    | `/research-projects`                                        | Get all research projects                | Admin, DeptHead, Faculty, Compliance, Regulator   |
| 3     | `GET`    | `/research-projects/{id}`                                   | Get a research project by id             | Admin, DeptHead, Faculty, Student (enrolled), Compliance |
| 4     | `PATCH`  | `/research-projects/{id}/status`                            | Update a research project status         | Admin, Faculty (own)                              |
| 5     | `PATCH`  | `/research-projects/{id}/verify`                            | Verify a research project                | Admin, Compliance                                 |
| 6     | `POST`   | `/research-projects/{id}/faculty`                           | Assign a co-investigator faculty         | Admin, Faculty (own)                              |
| 7     | `DELETE` | `/research-projects/{id}/faculty/{facultyId}`               | Remove a co-investigator faculty         | Admin, Faculty (own)                              |
| 8     | `POST`   | `/research-projects/{id}/students`                          | Add a student to a research project      | Admin, Faculty (own)                              |
| 9     | `DELETE` | `/research-projects/{id}/students/{studentId}`              | Remove a student from a research project | Admin, Faculty (own)                              |
| 10    | `DELETE` | `/research-projects/{id}`                                   | Delete a research project by id          | Admin                                             |

---

## Notifications API

| Sl No | Method   | Endpoint                            | Description                              | Role                              |
|:------|:---------|:------------------------------------|:-----------------------------------------|:----------------------------------|
| 1     | `POST`   | `/notifications`                    | Create a new notification                | Admin                             |
| 2     | `GET`    | `/notifications/me`                 | Get notifications for the current user   | All                               |
| 3     | `PATCH`  | `/notifications/{id}/read`          | Mark a notification as read              | All                               |
| 4     | `PATCH`  | `/notifications/read-all`           | Mark all notifications as read           | All                               |
| 5     | `DELETE` | `/notifications/{id}`               | Delete a notification by id              | Admin                             |

---

## Audit Logs API

> **Note:** Audit Logs are system-generated, immutable records of actions across the platform. No create/update/delete operations are permitted.

| Sl No | Method   | Endpoint                                    | Description                              | Role                        |
|:------|:---------|:--------------------------------------------|:-----------------------------------------|:----------------------------|
| 1     | `GET`    | `/audit-logs`                               | Get all audit log entries                | Admin, Compliance           |
| 2     | `GET`    | `/audit-logs/{id}`                          | Get an audit log entry by id             | Admin, Compliance           |
| 3     | `GET`    | `/audit-logs/user/{userId}`                 | Get audit log entries for a user         | Admin, Compliance           |
| 4     | `GET`    | `/audit-logs/resource/{resource}`           | Get audit logs for a resource by name    | Admin, Compliance           |

---

## Audits API

> **Note:** Audits are manually created compliance/review audits, distinct from system-generated Audit Logs.

| Sl No | Method   | Endpoint                    | Description                        | Role                               |
|:------|:---------|:----------------------------|:-----------------------------------|:-----------------------------------|
| 1     | `POST`   | `/audits`                   | Create a new audit                 | Admin, Compliance                  |
| 2     | `GET`    | `/audits`                   | Get all audits                     | Admin, Compliance, Regulator       |
| 3     | `GET`    | `/audits/{id}`              | Get an audit by id                 | Admin, Compliance, Regulator       |
| 4     | `PUT`    | `/audits/{id}`              | Fully update an audit by id        | Admin, Compliance                  |
| 5     | `PATCH`  | `/audits/{id}/status`       | Update the audit status            | Admin, Compliance                  |
| 6     | `DELETE` | `/audits/{id}`              | Delete an audit by id              | Admin                              |

---

## Compliance Records API

| Sl No | Method   | Endpoint                                          | Description                              | Role                               |
|:------|:---------|:--------------------------------------------------|:-----------------------------------------|:-----------------------------------|
| 1     | `POST`   | `/compliance-records`                             | Create a compliance record               | Admin, Compliance                  |
| 2     | `GET`    | `/compliance-records`                             | Get all compliance records               | Admin, Compliance, Regulator       |
| 3     | `GET`    | `/compliance-records/{id}`                        | Get a compliance record by id            | Admin, Compliance, Regulator       |
| 4     | `GET`    | `/compliance-records/entity/{entityId}`           | Get compliance records for an entity     | Admin, Compliance, Regulator       |
| 5     | `PUT`    | `/compliance-records/{id}`                        | Fully update a compliance record         | Admin, Compliance                  |
| 6     | `PATCH`  | `/compliance-records/{id}`                        | Partially update a compliance record     | Admin, Compliance                  |
| 7     | `DELETE` | `/compliance-records/{id}`                        | Delete a compliance record by id         | Admin                              |

---

## Reports API

| Sl No | Method   | Endpoint                                    | Description                        | Role                                        |
|:------|:---------|:--------------------------------------------|:-----------------------------------|:--------------------------------------------|
| 1     | `POST`   | `/reports`                                  | Create a new report                | Admin, Compliance                           |
| 2     | `GET`    | `/reports`                                  | Get all reports                    | Admin, Compliance, Regulator                |
| 3     | `GET`    | `/reports/{id}`                             | Get a report by id                 | Admin, Compliance, Regulator                |
| 4     | `GET`    | `/reports/department/{departmentId}`        | Get reports for a department       | Admin, DeptHead, Compliance, Regulator      |
| 5     | `PUT`    | `/reports/{id}`                             | Fully update a report by id        | Admin, Compliance                           |
| 6     | `PATCH`  | `/reports/{id}/status`                      | Update a report status             | Admin, Compliance                           |
| 7     | `DELETE` | `/reports/{id}`                             | Delete a report by id              | Admin                                       |