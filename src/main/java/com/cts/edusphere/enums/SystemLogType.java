package com.cts.edusphere.enums;

/**
 * Comprehensive catalogue of system log event types used throughout EduSphere.
 * Each constant is recorded in an {@code AuditLogRequestDTO} to classify the nature
 * of an operational event, error, or security incident for monitoring and audit purposes.
 *
 * <p>Constants are grouped loosely by domain:
 * <ul>
 *   <li>General infrastructure events (API access, database errors, auth failures)</li>
 *   <li>Entity-specific CRUD failure events (course, department, faculty, student, etc.)</li>
 *   <li>File storage and document events</li>
 *   <li>Notification events</li>
 *   <li>Compliance and audit events</li>
 * </ul>
 */
public enum SystemLogType {
    /** A successful or attempted API endpoint access was recorded. */
    API_ACCESS,
    /** An unexpected database-level exception occurred during an operation. */
    DATABASE_EXCEPTION,
    /** A request was made without valid authentication credentials. */
    UNAUTHORIZED_ACCESS,
    /** A user attempted an action that their assigned role does not permit. */
    ROLE_VIOLATION,
    /** The provided JWT or session token was invalid or malformed. */
    INVALID_TOKEN,
    /** Input validation failed for one or more request fields. */
    VALIDATION_FAILURE,
    /** An unclassified internal application error occurred. */
    INTERNAL_ERROR,
    /** A call to an external third-party service failed. */
    EXTERNAL_SERVICE_ERROR,
    /** The requested resource could not be located in the system. */
    RESOURCE_NOT_FOUND,
    /** An attempt was made to create a resource that already exists. */
    RESOURCE_ALREADY_EXISTS,
    /** A database constraint or data integrity rule was violated. */
    DATA_INTEGRITY_VIOLATION,
    /** A user account attempted access but the account is deactivated. */
    ACCOUNT_DEACTIVATED,
    /** Access to the requested resource was explicitly denied. */
    ACCESS_DENIED,
    /** A registration was attempted with an email address that is already in use. */
    EMAIL_ALREADY_EXISTS,
    /** The supplied password did not match the stored credential. */
    INVALID_PASSWORD,
    /** An exception occurred while interacting with the file storage service. */
    FILE_STORAGE_EXCEPTION,
    /** A generic HTTP 500 internal server error was encountered. */
    INTERNAL_SERVER_ERROR,
    /** An error occurred while attempting to change a user's password. */
    PASSWORD_CHANGE_ERROR,
    /** A user account could not be created due to a system error. */
    USER_NOT_CREATED,
    /** The uploaded file has an unsupported or invalid format. */
    INVALID_FILE_FORMAT,
    /** A requested file could not be found in storage. */
    FILE_NOT_FOUND,
    /** The requested department record does not exist. */
    DEPARTMENT_NOT_FOUND,
    /** The requested course record does not exist. */
    COURSE_NOT_FOUND,
    /** An attempt was made to create a course that already exists. */
    COURSE_ALREADY_EXISTS,
    /** The requested user account does not exist. */
    USER_NOT_FOUND,
    /** No new (unread) notifications were found for the user. */
    NO_NEW_NOTIFICATIONS_FOUND,
    /** No notifications of any kind were found for the user. */
    NO_NOTIFICATIONS_FOUND,
    /** A delete operation could not be completed. */
    FAILED_TO_DELETE,
    /** A concurrency lock prevented the operation from completing. */
    LOCKED_EXCEPTION,
    /** The requested audit record does not exist. */
    AUDIT_NOT_FOUND,
    /** No audit records were found matching the given criteria. */
    AUDITS_NOT_FOUND,
    /** An audit record could not be deleted. */
    AUDIT_NOT_DELETED,
    /** Creating a new audit record failed. */
    AUDIT_CREATE_FAILED,
    /** Creating a new audit log entry failed. */
    AUDIT_LOG_CREATE_FAILED,
    /** The requested compliance record does not exist. */
    COMPLAINCE_RECORD_NOT_FOUND,
    /** Creating a new compliance record failed. */
    COMPLAINCE_RECORD_NOT_CREATED,
    /** Updating an existing compliance record failed. */
    COMPLIANCE_RECORD_UPDATE_FAILED,
    /** Creating a new course failed. */
    COURSE_NOT_CREATED,
    /** No course records were found matching the given criteria. */
    COURSES_NOT_FOUND,
    /** No audit log entries were found matching the given criteria. */
    AUDIT_LOGS_NOT_FOUND,
    /** A compliance record could not be deleted. */
    COMPLAINCE_RECORD_NOT_DELETED,
    /** A course could not be deleted. */
    COURSE_NOT_DELETED,
    /** A course could not be updated. */
    COURSE_NOT_UPDATED,
    /** Creating a new curriculum entry failed. */
    CURRICULUM_NOT_CREATED,
    /** A curriculum entry could not be deleted. */
    CURRICULUM_NOT_DELETED,
    /** The requested curriculum entry does not exist. */
    CURRICULUM_NOT_FOUND,
    /** A curriculum entry could not be updated. */
    CURRICULUM_NOT_UPDATED,
    /** No curriculum entries were found matching the given criteria. */
    CURRICULUMS_NOT_FOUND,
    /** A department record could not be updated. */
    DEPARTMENT_COULD_NOT_BE_UPDATED,
    /** A department record could not be deleted. */
    DEPARTMENT_COULD_NOT_BE_DELETED,
    /** Creating a new department record failed. */
    DEPARTMENT_NOT_CREATED,
    /** An exam record could not be deleted. */
    EXAM_COULD_NOT_BE_DELETED,
    /** An exam record could not be updated. */
    EXAM_COULD_NOT_BE_UPDATED,
    /** No department records were found matching the given criteria. */
    DEPARTMENTS_NOT_FOUND,
    /** Creating a new exam record failed. */
    EXAM_NOT_CREATED,
    /** The requested exam record does not exist. */
    EXAM_NOT_FOUND,
    /** No exam records were found matching the given criteria. */
    EXAMS_NOT_FOUND,
    /** No faculty records were found matching the given criteria. */
    FACULTIES_NOT_FOUND,
    /** Creating a new faculty account failed. */
    FACULTY_NOT_CREATED,
    /** A faculty account could not be deleted. */
    FACULTY_NOT_DELETED,
    /** The requested faculty account does not exist. */
    FACULTY_NOT_FOUND,
    /** A faculty account could not be updated. */
    FACULTY_NOT_UPDATED,
    /** The faculty service dependency could not be resolved. */
    FACULTY_SERVICE_NOT_FOUND,
    /** Creating an audit log entry failed at the service level. */
    FAILED_TO_CREATE_LOG,
    /** The audit review process could not be completed. */
    FAILED_TO_REVIEW_AUDIT,
    /** A generic file storage operation error occurred. */
    FILE_STORAGE_ERROR,
    /** A grade record could not be deleted. */
    GRADE_COULD_NOT_BE_DELETED,
    /** Creating a new grade record failed. */
    GRADE_NOT_CREATED,
    /** A grade record could not be deleted. */
    GRADE_NOT_DELETED,
    /** A grade record could not be updated. */
    GRADE_NOT_UPDATED,
    /** No grade records were found matching the given criteria. */
    GRADES_NOT_FOUND,
    /** The user does not have sufficient privileges to perform the requested operation. */
    INSUFFICIENT_PERMISSION,
    /** The supplied email and password combination is incorrect. */
    INVALID_CREDENTIALS,
    /** No notification was found with the specified identifier. */
    NO_NOTIFICATION_FOUND_WITH_ID,
    /** Creating a new notification failed. */
    NOTIFICATION_NOT_CREATED,
    /** A notification could not be deleted. */
    NOTIFICATION_NOT_DELETED,
    /** The requested notification does not exist. */
    NOTIFICATION_NOT_FOUND,
    /** A notification could not be updated. */
    NOTIFICATION_NOT_UPDATED,
    /** No notification records were found matching the given criteria. */
    NOTIFICATIONS_NOT_FOUND,
    /** The requested compliance officer account does not exist. */
    OFFICER_NOT_FOUND,
    /** The password change operation could not be completed successfully. */
    PASSWORD_NOT_CHANGED,
    /** Creating a new report failed. */
    REPORT_CREATION_FAILED,
    /** A report could not be deleted. */
    REPORT_DELETION_FAILED,
    /** Fetching one or more reports failed. */
    REPORT_FETCHING_FAILED,
    /** The requested report does not exist. */
    REPORT_NOT_FOUND,
    /** A report could not be updated. */
    REPORT_UPDATING_FAILED,
    /** Creating a new research project failed. */
    RESEARCH_PROJECT_CREATION_FAILURE,
    /** A research project could not be deleted. */
    RESEARCH_PROJECT_DELETION_FAILED,
    /** The requested research project does not exist. */
    RESEARCH_PROJECT_NOT_FOUND,
    /** A research project could not be updated. */
    RESEARCH_PROJECT_UPDATE_FAILED,
    /** A generic storage layer error occurred. */
    STORAGE_ERROR,
    /** A file could not be found in the storage backend. */
    STORAGE_FILE_NOT_FOUND,
    /** Creating a new student account failed. */
    STUDENT_CREATION_FAILED,
    /** A student account could not be deleted. */
    STUDENT_DELETION_FAILED,
    /** A student document could not be deleted. */
    STUDENT_DOCUMENT_DELETION_FAILED,
    /** Downloading a student document failed. */
    STUDENT_DOCUMENT_DOWNLOAD_FAILED,
    /** The requested student document does not exist. */
    STUDENT_DOCUMENT_NOT_FOUND,
    /** No student documents were found matching the given criteria. */
    STUDENT_DOCUMENTS_NOT_FOUND,
    /** Uploading a student document failed. */
    STUDENT_DOCUMENT_UPLOAD_FAILED,
    /** The requested student account does not exist. */
    STUDENT_NOT_FOUND,
    /** No student records were found matching the given criteria. */
    STUDENTS_NOT_FOUND,
    /** A student account could not be updated. */
    STUDENT_UPDATE_FAILED,
    /** A user could not be subscribed to receive notifications. */
    SUBSCRIBING_TO_NOTIFICATION_FAILED,
    /** Creating a new thesis record failed. */
    THESES_CREATION_FAILED,
    /** A thesis record could not be deleted. */
    THESES_DELETION_FAILED,
    /** No thesis records were found matching the given criteria. */
    THESES_NOT_FOUND,
    /** A thesis record could not be updated. */
    THESES_UPDATION_FAILED,
    /** The provided JWT access token has expired. */
    TOKEN_EXPIRED,
    /** An attempt to update a compliance record failed. */
    UPDATING_COMPLIANCE_RECORD_FAILED,
    /** Creating a new user account failed. */
    USER_CREATION_FAILED,
    /** A user account could not be deleted. */
    USER_DELETION_FAILED,
    /** No user accounts were found matching the given criteria. */
    USERS_NOT_FOUND,
    /** A user account could not be updated. */
    USER_UPDATE_FAILED,
    /** Creating a new workload record failed. */
    WORKLOAD_CREATE_FAILED,
    /** Fetching a workload record failed. */
    WORKLOAD_FAILED_TO_FETCH,
    /** The requested workload record does not exist. */
    WORKLOAD_NOT_FOUND,
    /** Fetching multiple workload records failed. */
    WORKLOADS_FAILED_TO_FETCH,
}
