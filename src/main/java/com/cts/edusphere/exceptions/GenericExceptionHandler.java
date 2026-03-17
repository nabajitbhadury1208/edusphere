package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final GenericExceptionConfig exceptionConfig;

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

    // 400 (Bad Request) - Custom Exceptions
    @ExceptionHandler({
        InvalidPasswordException.class,
        StorageException.class
    })
    public ResponseEntity<?> handleBadRequest(RuntimeException ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.BAD_REQUEST, req);
    }

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

    // Global Fallback for Unhandled Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex, WebRequest req) {
        return exceptionConfig.processError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }
}