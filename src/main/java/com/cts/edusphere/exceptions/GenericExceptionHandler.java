package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.*;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global REST exception handler for the EduSphere application.
 *
 * <p>All {@link org.springframework.web.bind.annotation.RestController} exceptions
 * bubble up here and are mapped to appropriate HTTP status codes before being
 * returned as a structured {@link ErrorResponse} payload.  Each handler method
 * also triggers an audit-log entry via {@link GenericExceptionConfig}.</p>
 *
 * <p>Handler groupings by HTTP status:</p>
 * <ul>
 *   <li>400 Bad Request  – validation failures and invalid-password errors</li>
 *   <li>401 Unauthorized – bad credentials, invalid/expired tokens, locked accounts</li>
 *   <li>403 Forbidden    – access-denied, disabled accounts, insufficient permissions</li>
 *   <li>404 Not Found    – any domain entity that could not be located</li>
 *   <li>409 Conflict     – duplicate resource or data-integrity violations</li>
 *   <li>500 Internal     – operation failures (create/update/delete), file storage, etc.</li>
 * </ul>
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final GenericExceptionConfig exceptionConfig;

    /**
     * Handles bean-validation failures thrown by {@code @Valid} / {@code @Validated}
     * on request bodies or parameters.
     *
     * <p>Collects all field-level constraint violations into a map and returns them
     * as the {@code validationError} field of {@link ErrorResponse}.</p>
     *
     * @param ex  the validation exception containing per-field binding results
     * @param req the current web request
     * @return {@code 400 Bad Request} with a map of field → error message
     */
    // 400 (Bad Request) - Validation Failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        exceptionConfig.logSecurityEvent(
            SystemLogType.VALIDATION_FAILURE, Severity.WARN, "Validation failed");

        return exceptionConfig.buildErrorResponse(
            "Validation failed", HttpStatus.BAD_REQUEST, req, errors);
    }

    /**
     * Handles custom bad-request exceptions such as an invalid password format
     * or a storage operation error that is the caller's fault.
     *
     * @param ex  the runtime exception indicating a bad request condition
     * @param req the current web request
     * @return {@code 400 Bad Request} with the exception message
     */
    // 400 (Bad Request) - Custom Exceptions
    @ExceptionHandler({
        InvalidPasswordException.class,
        StorageException.class
    })
    public ResponseEntity<?> handleBadRequest(RuntimeException ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.BAD_REQUEST, req);
    }

    /**
     * Handles authentication failures, including bad credentials, invalid or
     * expired JWT tokens, and locked user accounts.
     *
     * @param ex  the exception indicating the request is unauthenticated
     * @param req the current web request
     * @return {@code 401 Unauthorized} with the exception message
     */
    // 401 (Unauthorized)
    @ExceptionHandler({
        BadCredentialsException.class,
        InvalidCredentialsException.class,
        InvalidTokenException.class,
        LockedException.class,
        TokenExpiredException.class
    })
    public ResponseEntity<?> handleUnauthorized(Exception ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.UNAUTHORIZED, req);
    }

    /**
     * Handles authorisation failures for authenticated users who lack the required
     * permissions, have disabled accounts, or attempt to access a forbidden resource.
     *
     * @param ex  the exception indicating insufficient access rights
     * @param req the current web request
     * @return {@code 403 Forbidden} with the exception message
     */
    // 403 (Forbidden)
    @ExceptionHandler({
        AccessDeniedException.class,
        DisabledException.class,
        InsufficientPermissionException.class,
        UnauthorizedAccessException.class
    })
    public ResponseEntity<?> handleForbidden(Exception ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.FORBIDDEN, req);
    }

    /**
     * Handles all "entity not found" exceptions across every domain in the
     * application (audits, compliance records, courses, students, users, etc.).
     *
     * @param ex      the exception indicating the requested resource was not found
     * @param request the current web request
     * @return {@code 404 Not Found} with the exception message
     */
    // 404 (Not Found)
    @ExceptionHandler({
        AuditLogNotFoundException.class,
        AuditLogsNotFoundException.class,
        AuditNotFoundException.class,
        AuditsNotFoundException.class,
        ComplianceRecordNotFoundException.class,
        ComplianceRecordsNotFoundException.class,
        CourseNotFoundException.class,
        CoursesNotFoundException.class,
        CurriculumNotFoundException.class,
        CurriculumsNotFoundException.class,
        DepartmentNotFoundException.class,
        DepartmentsNotFoundException.class,
        ExamNotFoundException.class,
        ExamsNotFoundException.class,
        FacultiesNotFoundException.class,
        FacultyNotFoundException.class,
        FacultyServiceNotFoundException.class,
        GradesNotFoundException.class,
        NoNotificationFoundWithId.class,
        NotificationNotFoundException.class,
        NotificationsNotFoundException.class,
        OfficerNotFoundException.class,
        ReportNotFoundException.class,
        ResearchProjectNotFoundException.class,
        ResourceNotFoundException.class,
        StorageFileNotFoundException.class,
        StudentDocumentNotFoundException.class,
        StudentDocumentsNotFoundException.class,
        StudentNotFoundException.class,
        StudentsNotFoundException.class,
        ThesisNotFoundException.class,
        UserNotFoundException.class,
        UsersNotFoundException.class,
        WorkLoadNotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
        return exceptionConfig.processError(ex, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles conflict exceptions raised when a resource already exists or a
     * database unique-constraint is violated.
     *
     * @param ex  the exception indicating a resource conflict
     * @param req the current web request
     * @return {@code 409 Conflict} with the exception message
     */
    // 409 (Conflict)
    @ExceptionHandler({
        CourseAlreadyExistsException.class,
        DataIntegrityViolationException.class,
        EmailAlreadyExistsException.class,
        ResourceAlreadyExistsException.class
    })
    public ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.CONFLICT, req);
    }

    /**
     * Handles all server-side operation failures: creation, update, and deletion
     * failures across every domain, as well as file-storage and external-service
     * errors and any uncategorised internal errors.
     *
     * @param ex  the exception describing the internal failure
     * @param req the current web request
     * @return {@code 500 Internal Server Error} with the exception message
     */
    // 500 (Internal Server Error)
    @ExceptionHandler({
        AuditNotDeletedException.class,
        CannotDeleteException.class,
        ComplianceRecordNotCreatedException.class,
        ComplianceRecordNotDeletedException.class,
        CourseNoCreatedException.class,
        CourseNotDeletedException.class,
        CourseNotUpdatedException.class,
        CurriculumNotCreatedException.class,
        CurriculumNotDeletedException.class,
        CurriculumNotUpdatedException.class,
        DepartmentCouldNotBeDeletedException.class,
        DepartmentCouldNotBeUpdatedException.class,
        DepartmentNotCreatedException.class,
        ExamCouldNotBeDeletedException.class,
        ExamCouldNotBeUpdated.class,
        ExamCouldNotBeUpdatedException.class,
        ExamNotCreatedException.class,
        ExternalServiceException.class,
        FacultyNotCreatedException.class,
        FacultyNotDeletedException.class,
        FacultyNotUpdatedException.class,
        FailedToCreateLogException.class,
        FailedToReviewAuditException.class,
        FileStorageException.class,
        GradeCouldNotBeDeletedException.class,
        GradeNotCreatedException.class,
        GradeNotDeletedException.class,
        GradeNotUpdatedException.class,
        InternalServerErrorException.class,
        NotificationNotCreatedException.class,
        NotificationNotDeletedException.class,
        NotificationNotUpdatedException.class,
        PasswordNotChangedException.class,
        ReportCreationFailedException.class,
        ReportDeletionFailedException.class,
        ReportFetchingFailedException.class,
        ReportUpdatingFailedException.class,
        ResearchProjectCreationFailureException.class,
        ResearchProjectDeletionFailed.class,
        ResearchProjectUpdateFailedException.class,
        StudentCreationFailedException.class,
        StudentDeletionFailedException.class,
        StudentDocumentDeletionFailedException.class,
        StudentDocumentDownloadFailedException.class,
        StudentDocumentUploadFailedException.class,
        StudentUpdateFailedException.class,
        SubscribingToNotificationFailed.class,
        ThesisCreationFailedException.class,
        ThesisDeletionFailedException.class,
        ThesisUpdationFailedException.class,
        UpdatingComplianceRecordFailedException.class,
        UserCreationFailedException.class,
        UserDeletionFailedException.class,
        UserUpdateFailedException.class,
        WorkLoadCreateFailedException.class,
        WorkLoadFailedToFetchException.class,
        WorkLoadsFailedToFetchException.class
    })
    public ResponseEntity<?> handleInternalError(RuntimeException ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    /**
     * Last-resort handler that catches any {@link Exception} not matched by a
     * more specific handler above.
     *
     * <p>Prevents raw stack traces from leaking to API consumers by returning a
     * consistent {@link ErrorResponse} structure.</p>
     *
     * @param ex  the uncaught exception
     * @param req the current web request
     * @return {@code 500 Internal Server Error} with the exception message
     */
    // Global Fallback for Unhandled Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }
}