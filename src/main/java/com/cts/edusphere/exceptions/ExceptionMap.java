package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.nio.file.AccessDeniedException;
import java.util.Map;

public final class ExceptionMap {

    private ExceptionMap() {
        // pvt const
    }

    public static final java.util.Map<Class<? extends Exception>, SystemLogType> LOG_MAPPINGS =
            Map.ofEntries(
                    Map.entry(AuditLogNotFoundException.class, SystemLogType.AUDIT_LOGS_NOT_FOUND),
                    Map.entry(AuditLogsNotFoundException.class, SystemLogType.AUDIT_LOGS_NOT_FOUND),
                    Map.entry(AuditNotDeletedException.class, SystemLogType.AUDIT_NOT_DELETED),
                    Map.entry(AuditNotFoundException.class, SystemLogType.AUDIT_NOT_FOUND),
                    Map.entry(AuditsNotFoundException.class, SystemLogType.AUDITS_NOT_FOUND),
                    Map.entry(CannotDeleteException.class, SystemLogType.FAILED_TO_DELETE),
                    Map.entry(
                            ComplianceRecordNotCreatedException.class,
                            SystemLogType.COMPLAINCE_RECORD_NOT_CREATED),
                    Map.entry(
                            ComplianceRecordNotDeletedException.class,
                            SystemLogType.COMPLAINCE_RECORD_NOT_DELETED),
                    Map.entry(
                            ComplianceRecordNotFoundException.class, SystemLogType.COMPLAINCE_RECORD_NOT_FOUND),
                    Map.entry(
                            ComplianceRecordsNotFoundException.class, SystemLogType.COMPLAINCE_RECORD_NOT_FOUND),
                    Map.entry(CourseAlreadyExistsException.class, SystemLogType.COURSE_ALREADY_EXISTS),
                    Map.entry(CourseNoCreatedException.class, SystemLogType.COURSE_NOT_CREATED),
                    Map.entry(CourseNotDeletedException.class, SystemLogType.COURSE_NOT_DELETED),
                    Map.entry(CourseNotFoundException.class, SystemLogType.COURSE_NOT_FOUND),
                    Map.entry(CourseNotUpdatedException.class, SystemLogType.COURSE_NOT_UPDATED),
                    Map.entry(CoursesNotFoundException.class, SystemLogType.COURSES_NOT_FOUND),
                    Map.entry(CurriculumNotCreatedException.class, SystemLogType.CURRICULUM_NOT_CREATED),
                    Map.entry(CurriculumNotDeletedException.class, SystemLogType.CURRICULUM_NOT_DELETED),
                    Map.entry(CurriculumNotFoundException.class, SystemLogType.CURRICULUM_NOT_FOUND),
                    Map.entry(CurriculumNotUpdatedException.class, SystemLogType.CURRICULUM_NOT_UPDATED),
                    Map.entry(CurriculumsNotFoundException.class, SystemLogType.CURRICULUMS_NOT_FOUND),
                    Map.entry(
                            DepartmentCouldNotBeDeletedException.class,
                            SystemLogType.DEPARTMENT_COULD_NOT_BE_DELETED),
                    Map.entry(
                            DepartmentCouldNotBeUpdatedException.class,
                            SystemLogType.DEPARTMENT_COULD_NOT_BE_UPDATED),
                    Map.entry(DepartmentNotCreatedException.class, SystemLogType.DEPARTMENT_NOT_CREATED),
                    Map.entry(DepartmentNotFoundException.class, SystemLogType.DEPARTMENT_NOT_FOUND),
                    Map.entry(DepartmentsNotFoundException.class, SystemLogType.DEPARTMENTS_NOT_FOUND),
                    Map.entry(EmailAlreadyExistsException.class, SystemLogType.EMAIL_ALREADY_EXISTS),
                    Map.entry(ExamCouldNotBeDeletedException.class, SystemLogType.EXAM_COULD_NOT_BE_DELETED),
                    Map.entry(ExamCouldNotBeUpdated.class, SystemLogType.EXAM_COULD_NOT_BE_UPDATED),
                    Map.entry(ExamCouldNotBeUpdatedException.class, SystemLogType.EXAM_COULD_NOT_BE_UPDATED),
                    Map.entry(ExamNotCreatedException.class, SystemLogType.EXAM_NOT_CREATED),
                    Map.entry(ExamNotFoundException.class, SystemLogType.EXAM_NOT_FOUND),
                    Map.entry(ExamsNotFoundException.class, SystemLogType.EXAMS_NOT_FOUND),
                    Map.entry(ExternalServiceException.class, SystemLogType.EXTERNAL_SERVICE_ERROR),
                    Map.entry(FacultiesNotFoundException.class, SystemLogType.FACULTIES_NOT_FOUND),
                    Map.entry(FacultyNotCreatedException.class, SystemLogType.FACULTY_NOT_CREATED),
                    Map.entry(FacultyNotDeletedException.class, SystemLogType.FACULTY_NOT_DELETED),
                    Map.entry(FacultyNotFoundException.class, SystemLogType.FACULTY_NOT_FOUND),
                    Map.entry(FacultyNotUpdatedException.class, SystemLogType.FACULTY_NOT_UPDATED),
                    Map.entry(FacultyServiceNotFoundException.class, SystemLogType.FACULTY_SERVICE_NOT_FOUND),
                    Map.entry(FailedToCreateLogException.class, SystemLogType.FAILED_TO_CREATE_LOG),
                    Map.entry(FailedToReviewAuditException.class, SystemLogType.FAILED_TO_REVIEW_AUDIT),
                    Map.entry(FileStorageException.class, SystemLogType.FILE_STORAGE_ERROR),
                    Map.entry(
                            GradeCouldNotBeDeletedException.class, SystemLogType.GRADE_COULD_NOT_BE_DELETED),
                    Map.entry(GradeNotCreatedException.class, SystemLogType.GRADE_NOT_CREATED),
                    Map.entry(GradeNotDeletedException.class, SystemLogType.GRADE_NOT_DELETED),
                    Map.entry(GradeNotUpdatedException.class, SystemLogType.GRADE_NOT_UPDATED),
                    Map.entry(GradesNotFoundException.class, SystemLogType.GRADES_NOT_FOUND),
                    Map.entry(InsufficientPermissionException.class, SystemLogType.INSUFFICIENT_PERMISSION),
                    Map.entry(InternalServerErrorException.class, SystemLogType.INTERNAL_SERVER_ERROR),
                    Map.entry(InvalidCredentialsException.class, SystemLogType.INVALID_CREDENTIALS),
                    Map.entry(InvalidPasswordException.class, SystemLogType.INVALID_PASSWORD),
                    Map.entry(InvalidTokenException.class, SystemLogType.INVALID_TOKEN),
                    Map.entry(NoNotificationFoundWithId.class, SystemLogType.NO_NOTIFICATION_FOUND_WITH_ID),
                    Map.entry(NotificationNotCreatedException.class, SystemLogType.NOTIFICATION_NOT_CREATED),
                    Map.entry(NotificationNotDeletedException.class, SystemLogType.NOTIFICATION_NOT_DELETED),
                    Map.entry(NotificationNotFoundException.class, SystemLogType.NOTIFICATION_NOT_FOUND),
                    Map.entry(NotificationNotUpdatedException.class, SystemLogType.NOTIFICATION_NOT_UPDATED),
                    Map.entry(NotificationsNotFoundException.class, SystemLogType.NOTIFICATIONS_NOT_FOUND),
                    Map.entry(OfficerNotFoundException.class, SystemLogType.OFFICER_NOT_FOUND),
                    Map.entry(PasswordNotChangedException.class, SystemLogType.PASSWORD_NOT_CHANGED),
                    Map.entry(ReportCreationFailedException.class, SystemLogType.REPORT_CREATION_FAILED),
                    Map.entry(ReportDeletionFailedException.class, SystemLogType.REPORT_DELETION_FAILED),
                    Map.entry(ReportFetchingFailedException.class, SystemLogType.REPORT_FETCHING_FAILED),
                    Map.entry(ReportNotFoundException.class, SystemLogType.REPORT_NOT_FOUND),
                    Map.entry(ReportUpdatingFailedException.class, SystemLogType.REPORT_UPDATING_FAILED),
                    Map.entry(
                            ResearchProjectCreationFailureException.class,
                            SystemLogType.RESEARCH_PROJECT_CREATION_FAILURE),
                    Map.entry(
                            ResearchProjectDeletionFailed.class, SystemLogType.RESEARCH_PROJECT_DELETION_FAILED),
                    Map.entry(
                            ResearchProjectNotFoundException.class, SystemLogType.RESEARCH_PROJECT_NOT_FOUND),
                    Map.entry(
                            ResearchProjectUpdateFailedException.class,
                            SystemLogType.RESEARCH_PROJECT_UPDATE_FAILED),
                    Map.entry(ResourceAlreadyExistsException.class, SystemLogType.RESOURCE_ALREADY_EXISTS),
                    Map.entry(ResourceNotFoundException.class, SystemLogType.RESOURCE_NOT_FOUND),
                    Map.entry(StorageException.class, SystemLogType.STORAGE_ERROR),
                    Map.entry(StorageFileNotFoundException.class, SystemLogType.STORAGE_FILE_NOT_FOUND),
                    Map.entry(StudentCreationFailedException.class, SystemLogType.STUDENT_CREATION_FAILED),
                    Map.entry(StudentDeletionFailedException.class, SystemLogType.STUDENT_DELETION_FAILED),
                    Map.entry(
                            StudentDocumentDeletionFailedException.class,
                            SystemLogType.STUDENT_DOCUMENT_DELETION_FAILED),
                    Map.entry(
                            StudentDocumentDownloadFailedException.class,
                            SystemLogType.STUDENT_DOCUMENT_DOWNLOAD_FAILED),
                    Map.entry(
                            StudentDocumentNotFoundException.class, SystemLogType.STUDENT_DOCUMENT_NOT_FOUND),
                    Map.entry(
                            StudentDocumentsNotFoundException.class, SystemLogType.STUDENT_DOCUMENTS_NOT_FOUND),
                    Map.entry(
                            StudentDocumentUploadFailedException.class,
                            SystemLogType.STUDENT_DOCUMENT_UPLOAD_FAILED),
                    Map.entry(StudentNotFoundException.class, SystemLogType.STUDENT_NOT_FOUND),
                    Map.entry(StudentsNotFoundException.class, SystemLogType.STUDENTS_NOT_FOUND),
                    Map.entry(StudentUpdateFailedException.class, SystemLogType.STUDENT_UPDATE_FAILED),
                    Map.entry(
                            SubscribingToNotificationFailed.class,
                            SystemLogType.SUBSCRIBING_TO_NOTIFICATION_FAILED),
                    Map.entry(ThesisCreationFailedException.class, SystemLogType.THESES_CREATION_FAILED),
                    Map.entry(ThesisDeletionFailedException.class, SystemLogType.THESES_DELETION_FAILED),
                    Map.entry(ThesisNotFoundException.class, SystemLogType.THESES_NOT_FOUND),
                    Map.entry(ThesisUpdationFailedException.class, SystemLogType.THESES_UPDATION_FAILED),
                    Map.entry(TokenExpiredException.class, SystemLogType.TOKEN_EXPIRED),
                    Map.entry(UnauthorizedAccessException.class, SystemLogType.UNAUTHORIZED_ACCESS),
                    Map.entry(
                            UpdatingComplianceRecordFailedException.class,
                            SystemLogType.UPDATING_COMPLIANCE_RECORD_FAILED),
                    Map.entry(UserCreationFailedException.class, SystemLogType.USER_CREATION_FAILED),
                    Map.entry(UserDeletionFailedException.class, SystemLogType.USER_DELETION_FAILED),
                    Map.entry(UserNotFoundException.class, SystemLogType.USER_NOT_FOUND),
                    Map.entry(UsersNotFoundException.class, SystemLogType.USERS_NOT_FOUND),
                    Map.entry(UserUpdateFailedException.class, SystemLogType.USER_UPDATE_FAILED),
                    Map.entry(WorkLoadCreateFailedException.class, SystemLogType.WORKLOAD_CREATE_FAILED),
                    Map.entry(WorkLoadFailedToFetchException.class, SystemLogType.WORKLOAD_FAILED_TO_FETCH),
                    Map.entry(WorkLoadNotFoundException.class, SystemLogType.WORKLOAD_NOT_FOUND),
                    Map.entry(
                            WorkLoadsFailedToFetchException.class, SystemLogType.WORKLOADS_FAILED_TO_FETCH),
                    Map.entry(AccessDeniedException.class, SystemLogType.ACCESS_DENIED),

                    Map.entry(BadCredentialsException.class, SystemLogType.INVALID_CREDENTIALS),
                    Map.entry(DataIntegrityViolationException.class, SystemLogType.DATA_INTEGRITY_VIOLATION),
                    Map.entry(DisabledException.class, SystemLogType.ACCOUNT_DEACTIVATED),
                    Map.entry(LockedException.class, SystemLogType.LOCKED_EXCEPTION)
            );

}
