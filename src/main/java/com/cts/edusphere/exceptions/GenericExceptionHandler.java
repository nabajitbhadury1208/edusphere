package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.*;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.cts.edusphere.services.audit_log.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GenericExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Autowired
    @Lazy
    private AuditLogService auditLogService;

    public void logSecurityEvent(SystemLogType logType, Severity severity, String details) {
        try {
            auditLogService.logSystemEvent(logType, severity, "EXCEPTION_HANDLER", "GenericExceptionHandler", details,
                    null);
        } catch (Exception e) {
            logger.error("Failed to write security level audit log {}", e.getMessage());
        }
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request,
            Map<String, String> validationError) {
        ErrorResponse response = ErrorResponse.builder().timeStamp(Instant.now()).status(status.value())
                .error(status.getReasonPhrase()).message(message)
                .path(request.getDescription(false).replace("uri=", "")).validationError(validationError).build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        logSecurityEvent(SystemLogType.INTERNAL_ERROR, Severity.ERROR, "An unexpected error occured");
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request, null);

    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
            WebRequest request) {
        logger.warn("ResourceNotFoundException: {}", exception.getMessage());
        String message = "Resource not found";
        logSecurityEvent(SystemLogType.RESOURCE_NOT_FOUND, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.NOT_FOUND, request, null);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex,
            WebRequest request) {
        logger.warn("ResourceAlreadyExistsException: {}", ex.getMessage());
        String message = "Resource already exists";
        logSecurityEvent(SystemLogType.RESOURCE_ALREADY_EXISTS, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
            WebRequest request) {
        logger.warn("DataIntegrityViolationException: {}", ex.getMessage());
        String message = "Database integrity violation: Likely a duplicate entry or missing required field.";
        logSecurityEvent(SystemLogType.DATA_INTEGRITY_VIOLATION, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        logger.warn("InvalidCredentialsException: {}", ex.getMessage());
        String message = "Invalid credentials provided";
        logSecurityEvent(SystemLogType.DATA_INTEGRITY_VIOLATION, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        logger.warn("BadCredentialsException: {}", ex.getMessage());
        String message = "Invalid credentials provided";
        logSecurityEvent(SystemLogType.UNAUTHORIZED_ACCESS, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(DisabledException ex, WebRequest request) {
        logger.warn("DisabledException: {}", ex.getMessage());
        String message = "Account is deactivated. Please contact support.";
        logSecurityEvent(SystemLogType.ACCOUNT_DEACTIVATED, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex, WebRequest request) {
        logger.warn("InvalidTokenException: {}", ex.getMessage());
        String message = "Invalid authentication token";
        logSecurityEvent(SystemLogType.INVALID_TOKEN, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(TokenExpiredException ex, WebRequest request) {
        logger.warn("TokenExpiredException: {}", ex.getMessage());
        String message = "Authentication token has expired";
        logSecurityEvent(SystemLogType.INVALID_TOKEN, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex, WebRequest request) {
        logger.warn("UnauthorizedAccessException: {}", ex.getMessage());
        String message = "Access is denied";
        logSecurityEvent(SystemLogType.UNAUTHORIZED_ACCESS, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    // Todo: Exception repeated here (Same as UnauthorizedAccessException)
    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPermission(InsufficientPermissionException ex,
            WebRequest request) {
        logger.warn("InsufficientPermissionException: {}", ex.getMessage());
        String message = "Insufficient permissions to perform this action";
        logSecurityEvent(SystemLogType.ACCESS_DENIED, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    // Todo: Exception repeated here (Same as UnauthorizedAccessException)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        logger.warn("AccessDeniedException: {}", ex.getMessage());
        String message = "Access is denied";
        logSecurityEvent(SystemLogType.ACCESS_DENIED, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {
        logger.warn("EmailAlreadyExistsException: {}", ex.getMessage());
        String message = "Email already exists";
        logSecurityEvent(SystemLogType.EMAIL_ALREADY_EXISTS, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
        logger.warn("InvalidPasswordException: {}", ex.getMessage());
        String message = "Invalid password provided";
        logSecurityEvent(SystemLogType.INVALID_PASSWORD, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex,
            WebRequest request) {
        logger.error("ExternalServiceException: {}", ex.getMessage());
        String message = "External service error";
        logSecurityEvent(SystemLogType.EXTERNAL_SERVICE_ERROR, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.BAD_GATEWAY, request, null);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex, WebRequest request) {
        logger.error("FileStorageException: {}", ex.getMessage());
        String message = "File storage error";
        logSecurityEvent(SystemLogType.FILE_STORAGE_EXCEPTION, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex,
            WebRequest request) {
        logger.error("InternalServerErrorException: {}", ex.getMessage());
        String message = "Internal server error";
        logSecurityEvent(SystemLogType.INTERNAL_SERVER_ERROR, Severity.ERROR, message);
        return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    @ExceptionHandler(PasswordCannotBeChangedException.class)
    public ResponseEntity<ErrorResponse> handlePasswordCannotBeChanged(PasswordCannotBeChangedException ex,
            WebRequest request) {
        logger.error("PasswordCannotBeChangedException: {}", ex.getMessage());
        logSecurityEvent(SystemLogType.PASSWORD_CHANGE_ERROR, Severity.ERROR, ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotCreated(UserNotCreatedException ex, WebRequest request) {
        logger.error("UserNotCreatedException: {}", ex.getMessage());
        logSecurityEvent(SystemLogType.USER_NOT_CREATED, Severity.ERROR, ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileFormat(StorageException ex, WebRequest request) {
        logger.error("InvalidFileFormatException: {}", ex.getMessage());
        logSecurityEvent(SystemLogType.INVALID_FILE_FORMAT, Severity.ERROR, ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStorageFileNotFound(StorageFileNotFoundException ex,
            WebRequest request) {
        logger.error("StorageFileNotFoundException: {}", ex.getMessage());
        logSecurityEvent(SystemLogType.FILE_NOT_FOUND, Severity.ERROR, ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("MethodArgumentNotValidException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.VALIDATION_FAILURE, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFoundException(DepartmentNotFoundException ex,
            WebRequest request) {

        logSecurityEvent(SystemLogType.DEPARTMENT_NOT_FOUND, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Fetching department failed", HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(MethodArgumentNotValidException ex,
            WebRequest request) {
        logger.warn("CourseNotFoundException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.COURSE_NOT_FOUND, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Fetching course by Id failed", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCourseAlreadyExsistsException(MethodArgumentNotValidException ex,
            WebRequest request) {
        logger.warn("CourseAlreadyExistsException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.COURSE_ALREADY_EXISTS, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Course with provided name already exists", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.USER_NOT_FOUND, Severity.WARN, ex.getMessage());
        return buildErrorResponse("User with provided details doesn't exist", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(NoNotificationFoundWithUserId.class)
    public ResponseEntity<ErrorResponse> handleNoNewNotificationFoundWithId(MethodArgumentNotValidException ex,
            WebRequest request) {
        logger.warn("NoNewNotificationFoundWithId: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.NO_NEW_NOTIFICATIONS_FOUND, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Notification with provided details of User doesn't exist", HttpStatus.BAD_REQUEST,
        request, errors);
    }
    
    @ExceptionHandler(NoNotificationFoundWithId.class)
    public ResponseEntity<ErrorResponse> handleNoNotificationFoundWithId(MethodArgumentNotValidException ex,
        WebRequest request) {
            logger.warn("NoNotificationFoundWithId: {}", ex.getMessage());
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logSecurityEvent(SystemLogType.NO_NOTIFICATIONS_FOUND, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Notification with provided details doesn't exist", HttpStatus.BAD_REQUEST, request,
        errors);
    }

    @ExceptionHandler(CannotDeleteException.class)
    public ResponseEntity<ErrorResponse> handleCannotDeleteException(MethodArgumentNotValidException ex,
        WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        logSecurityEvent(SystemLogType.FAILED_TO_DELETE, Severity.WARN, ex.getMessage());
        return buildErrorResponse("Deletion failed", HttpStatus.EXPECTATION_FAILED, request, errors);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, String>> handleLockedException(LockedException ex) {
        logSecurityEvent(SystemLogType.LOCKED_EXCEPTION, Severity.WARN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Your account has been deactivated. Please contact an admin."));
    }

}
