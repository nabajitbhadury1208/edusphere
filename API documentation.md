### Auth API
|Sl No | Method  | Endpoint 		    		| Description 					|  Role |
|  :---|:--------| :--- 		    		| :--- 						|  :--- |
|  1   | `POST`  | `/auth/register`         		| Register the user 				|  All  |
|  2   | `POST`  | `/auth/login`            		| Log the user in 				|  All  |
|  3   | `POST`  | `/auth/logout`           		| Logs out the user 				|  All  |
|  4   | `POST`  | `/auth/refresh`          		| Refresh access token 				|  All  |
|  5   | `PATCH` | `/auth/change-password`  		| Change the user password 			|  All  |

### User API
|Sl No | Method   | Endpoint 		    		            | Description 					|  Role |
|  :---|:---------|:-----------------------------| :--- 						|  :--- |
|  1   | `POST`   | `/users` 	   	    		         | Create a new user 				| Admin |
|  2   | `GET`    | `/users` 	   	    		         | Get all the users 				| Admin |
|  3   | `GET`    | `/users/{id}` 	    		        | Get User by id				| Admin |
|  4   | `PUT`    | `/users/{id}` 	    		        | Update a user by id				| Admin |
|  5   | `PATCH`  | `/users/{id}/status` 	    		 | Activate/Deactivate a user 			| Admin |
|  6   | `Delete` | `/users/{id}` 	    		        | Get the a user by id				| Admin |
|  7   | `PATCH`  | `/users/{id}` 	    		        | Update a user by id				| Admin |

### Student API
|Sl No | Method   | Endpoint 		    		| Description 					|  Role |
|  :---|:---------| :--- 		    		| :--- 						|  :--- |
|  1   | `POST`   | `/students` 	   	    		| Create a student 				| Admin |
|  2   | `GET`    | `/students` 	   	    		| Get all the students 				| Admin |
|  3   | `GET`    | `/students/{id}` 	    		| Get Student by id				| Admin |
|  4   | `POST`   | `/students/{id}` 	    		| Update a Student by id			| Admin |
|  5   | `DELETE` | `/students/{id}` 	    		| Delete a student				| Admin |
|  6   | `PATCH`  | `/students/{id}` 	   	 	| Update a student by id			| Admin |

### Faculty API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/faculties` 	    	    	| Create a new faculty	 			| Admin |
|  2   | `GET`    | `/faculties` 	    	    	| Get all the faculties 			| Admin |
|  3   | `GET`    | `/faculties/{id}` 	    	    	| Get Faculty by id				| Admin |
|  4   | `POST`   | `/faculties/{id}` 	    	    	| Update a Faculty by id			| Admin |
|  5   | `Delete` | `/faculties/{id}` 	    	    	| Delete a Faculty				| Admin |
|  6   | `PATCH`  | `/faculties/{id}` 	    	    	| Update a Faculty by id			| Admin |

### Department API
|Sl No | Method  | Endpoint 		            	          | Description 					|  Role |
|  :---|  :---   |:----------------------------------| :--- 						|  :--- |
|  1   |  `POST` | `/departments` 	    	    	        | Create a new department			| Admin |
|  2   |  `GET`  | `/departments` 	    	    	        | Get all the departments 			| Admin |
|  3   |  `GET`  | `/departments/{id}` 	    	    	   | Get Department by id				| Admin |
|  4   |  `POST` | `/departments/{id}` 	    	    	   | Update a Department by id			| Admin |
|  5   | `Delete`| `/departments/{id}` 	    	    	   | Delete a Department				| Admin |
|  6   |  `POST` | `/departments/{id}` 	    	    	   | Update a Department by id			| Admin |
|  7   | `PATCH` | `/departments/{id}/head` 	   	    | Change department head			| Admin |
|  8   |  `GET`  | `/departments/{id}/faculty` 	   	 | Get list of faculties of a department		| Admin |
|  9   |  `GET`  | `/departments/{id}/courses` 	   	 | Get list of courses of a department		| Admin |

### Courses API
|Sl No | Method  | Endpoint 		    	    	| Description 					|  Role |
|  :---|  :---   | :--- 		    	    	| :--- 						|  :--- |
|  1   |  `POST`  | `/courses` 	    		    	| Create a new course	 			| Admin |
|  2   |  `GET`  | `/courses` 	    		    	| Get all the Courses	 			| Admin |
|  3   |  `GET`  | `/courses/{id}` 	    	    	| Get Course by id				| Admin |
|  4   |  `POST` | `/courses/{id}` 	    	    	| Update a Course by id				| Admin |
|  5   | `Delete`| `/courses/{id}` 	    	    	| Delete a Course				| Admin |
|  6   | `PATCH` | `/courses/{id}` 	    		| Activate/Deactivate a Course 			| Admin |

### Curriculum API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/curriculum` 	    		| Create a new curriculum	 		| Admin |
|  2   | `GET`    | `/curriculum` 	    		| Get all the Curriculums	 		| Admin |
|  3   | `GET`    | `/curriculum/{id}` 	    	    	| Get Curriculum by id				| Admin |
|  4   | `PATCH`  | `/curriculum/{id}` 	    	    	| Update a Curriculum by id			| Admin |
|  5   | `Delete` | `/curriculum/{id}` 	    	    	| Delete a Curriculum				| Admin ||

### Exams API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/exams` 	    			| Create a new exam	 			| Admin |
|  2   | `GET`    | `/exams` 	    			| Get all the Exam	 			| Admin |
|  3   | `GET`    | `/exams/{id}` 	    	    	| Get Exam by id				| Admin |
|  4   | `PATCH`  | `/exams/{id}` 	    	    	| Update a Exam by id				| Admin |
|  5   | `Delete` | `/exams/{id}` 	    	    	| Delete a Exam					| Admin |
|  6   | `PATCH`  | `/exams/{id}` 	    	    	| Update a Exam by id				| Admin |
|  7   | `PATCH`  | `/exams/{id}` 	    		| Activate/Deactivate a Exam 			| Admin |
|  8   | `GET`    | `/exams/course/{courseId}` 	    	| Get Exam for a course by courseId 		| Admin |

### Grades API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/grades` 	    			| Submit a grade(Create)	 		| Admin |
|  2   | `GET`    | `/grades/students/{id}` 	    	| Get all grates of a Student			| Admin |
|  3   | `PATCH`  | `/grades/{id}` 	    	    	| Update a Grade by id				| Admin |
|  4   | `Delete` | `/grades/{id}` 	    	    	| Delete a Grade				| Admin |
|  5   | `GET`    | `/grades/exam/{id}` 	    	    	| Get all exam grades of Students		| Admin |
|  6   | `GET`    | `/grades/{id}` 	    	    	| Get all grade					| Admin |


### Workload API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/workload` 	    			| Create a new workload(asign to faculty)	| Admin |
|  3   | `GET`    | `/workload/{id}` 	    	    	| Get workload by id				| Admin |
|  3   | `GET`    | `/workload/faculty/{facultyId}` 	| Get workload for a faculty by facultyId	| Admin |
|  4   | `PATCH`  | `/workload/{id}` 	    	    	| Update a workload by id			| Admin |
|  5   | `Delete` | `/workload/{id}` 	    	    	| Delete a workload				| Admin |

### Thesis API
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/thesis` 	    			| Create a new thesis				| Admin |
|  2   | `GET`    | `/thesis/{id}` 	    	    	| Get thesis by id				| Admin |
|  3   | `GET`    | `/thesis/student/{studentId}` 	| Get all thesis for a student by studentId	| Admin |
|  4   | `GET`    | `/thesis/supervisor/{facultyId}` 	| Get all thesis for a faculty supervisor	| Admin |
|  5   | `PUT`    | `/thesis/{id}` 	    	    	| Update a thesis by id				| Admin |
|  6   | `Delete` | `/thesis/{id}` 	    	    	| Delete a thesis				| Admin |

### Student Documents
|Sl No | Method  | Endpoint 		    	    	| Description 					|  Role |
|  :---|  :---   | :--- 		    	    	| :--- 						|  :--- |
|  1   |  `POST` | `/student-documents` 	    	| Create a new Student Document			| Admin |
|  2   |  `GET`  | `/student-documents/{id}` 	    	| Get Student Document by id			| Admin |
|  3   |  `GET`  | `/student-documents/student/{studentId}` 	| Get all Document for a student by studentId			| Admin |
|  4   | `PATCH` | `/student-documents/{id}/verify` 	| Verify the student document			| Admin |
|  5   | `Delete`| `/student-documents/{id}` 	    	| Delete a document				| Admin |

### Research Projects
|Sl No | Method  | Endpoint 		    	    	| Description 					|  Role |
|  :---|  :---   | :--- 		    	    	| :--- 						|  :--- |
|  1   |  `POST` | `/research-projects` 	    	| Create a new Research Project			| Admin |
|  2   |  `GET`  | `/research-projects` 	    	| Get list of Research Project			| Admin |
|  3   |  `GET`  | `/research-projects/{id}` 		| Get Research project by id			| Admin |
|  4   | `PATCH` | `/research-projects/{id}/verify` 	| Verify the Research Project			| Admin |
|  5   | `Delete`| `/research-projects/{id}` 	    	| Delete a Research Project			| Admin |
|  1   | `PATCH` | `/research-projects/{id}/status` 	| Update a Research Project Status		| Admin |
|  2   |  `POST` | `/research-projects/{id}/faculty` 	| Assign co-investigator faculty		| Admin |
|  3   | `Delete`| `/research-projects/{id}/faculty/{facultyId}` 		| Delete co-investigator			| Admin |
|  4   | `POST`  | `/research-projects/{id}/students` 	| Add students to research project		| Admin |
|  5   | `Delete`| `/research-projects/{id}/students/{studentId}` 	    	| Delete a student Research Project		| Admin |

### Notifications
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/notifications` 	    		| Create a new notification			| Admin |
|  3   | `GET`    | `/notifications/me` 	    	    	| Get notifications for current user		| Admin |
|  3   | `PATCH`  | `/notifications/{id}/read` 		| Mark notifications as read			| Admin |
|  4   | `PATCH`  | `/notifications/{id}/read-all` 	| Mark all notifications as read		| Admin |
|  5   | `Delete` | `/notifications/{id}` 	    	| Delete a notification				| Admin |

### Audit Logs
|Sl No | Method  | Endpoint 		    	    	| Description 					|  Role |
|  :---|  :---   | :--- 		    	    	| :--- 						|  :--- |
|  1   |  `GET`  | `/audit-logs` 	    	    	| Get list of all audit logs			| Admin |
|  2   |  `GET`  | `/audit-logs/{id}` 			| Get audit logs entry by id			| Admin |
|  3   |  `GET`  | `/audit-logs/user/{userId}` 		| Get audit logs entry for a user by userId	| Admin |
|  4   |  `GET`  | `/audit-logs/resource/{resource}` 	| Get audit logs for a resource by name		| Admin |

### Audits
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/audits` 	    			| Create a new audit				| Admin |
|  2   | `GET`    | `/audits` 	    	    		| Get all audits				| Admin |
|  3   | `GET`    | `/audits/{id}` 	    	    	| Get a audit via auditId			| Admin |
|  4   | `PATCH`  | `/audits/{id}/status` 		| Update the audit status			| Admin |
|  5   | `PUT`    | `/audits/{id}`		 	| Update audit details				| Admin |
|  6   | `Delete` | `/audits/{id}`	 	    	| Delete an audit				| Admin |

### Compliance Records
|Sl No | Method  | Endpoint 		    	    	| Description 					|  Role |
|  :---|  :---   | :--- 		    	    	| :--- 						|  :--- |
|  1   |  `POST` | `/compliance-records` 	    	| Create a Compliance Record			| Admin |
|  2   |  `GET`  | `/compliance-records` 	    	| Get all Compliance Records list		| Admin |
|  3   |  `GET`  | `/compliance-records/{id}` 	    	| Get a Compliance Record via id			| Admin |
|  4   |  `GET`  | `/compliance-records/entity/{entityId}` 	| Get compliance record for an entity   | Admin |
|  5   |  `PUT`  | `/compliance-records/{id}`		| Update compliance records details		| Admin |
|  6   | `Delete`| `/compliance-records/{id}`	 	| Delete an compliance record			| Admin |

### Reports
|Sl No | Method   | Endpoint 		    	    	| Description 					|  Role |
|  :---|:---------| :--- 		    	    	| :--- 						|  :--- |
|  1   | `POST`   | `/reports` 	    			| Create a new report				| Admin |
|  2   | `GET`    | `/reports` 	    	    		| Get all report list				| Admin |
|  3   | `GET`    | `/reports/{id}` 	    	    	| Get report by id				| Admin |
|  4   | `GET`    | `reports/department/{departmentId}` 	| Get report by department			| Admin |
|  5   | `PATCH`  | `/reports/{id}/status`		| Update a report status			| Admin |
|  6   | `Delete` | `/reports/{id}`	 	    	| Delete a report				| Admin |
|  7   | `PUT`    | `/reports/{id}`			| Update a report 				| Admin |
