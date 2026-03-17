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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      MethodArgumentNotValidException ex, WebRequest req) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    exceptionConfig.logSecurityEvent(
        SystemLogType.VALIDATION_FAILURE, Severity.WARN, "Validation failed");

    return exceptionConfig.buildErrorResponse(
        "Validation failed", HttpStatus.BAD_REQUEST, req, errors);
  }

  // --- Global Fallback ---
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
    // exceptionConfig.logSecurityEvent(SystemLogType.INTERNAL_ERROR, Severity.ERROR, "Unexpected
    // error occurred");
    // return exceptionConfig.buildErrorResponse(
    //     "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, req, null);
  }

  // 404 (Not Found) --
  @ExceptionHandler({
    ResourceNotFoundException.class,
    UserNotFoundException.class,
    CourseNotFoundException.class,
    CoursesNotFoundException.class,
    DepartmentNotFoundException.class,
    StorageFileNotFoundException.class,
    AuditNotFoundException.class,
    AuditsNotFoundException.class,
    AuditLogNotFoundException.class,
    AuditLogsNotFoundException.class,
    ComplianceRecordNotFoundException.class,
    ComplianceRecordsNotFoundException.class,
    CurriculumNotFoundException.class,
    CurriculumsNotFoundException.class,
})
  public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
    return exceptionConfig.processError(ex, HttpStatus.NOT_FOUND, request);
  }

  // --- 409 (Conflict) ---
  @ExceptionHandler({
    ResourceAlreadyExistsException.class,
    DataIntegrityViolationException.class,
    EmailAlreadyExistsException.class
  })
  public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.CONFLICT, req);
  }

  // --- 401 (Unauthorized) ---
  @ExceptionHandler({
    BadCredentialsException.class,
    InvalidCredentialsException.class,
    InvalidTokenException.class,
    TokenExpiredException.class,
    LockedException.class
  })
  public ResponseEntity<ErrorResponse> handleUnauthorized(Exception ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.UNAUTHORIZED, req);
  }

  // --- 403 (Forbidden) ---
  @ExceptionHandler({
    UnauthorizedAccessException.class,
    InsufficientPermissionException.class,
    AccessDeniedException.class,
    DisabledException.class
  })
  public ResponseEntity<ErrorResponse> handleForbidden(Exception ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.FORBIDDEN, req);
  }

  // --- 400 (Bad Request) ---
  @ExceptionHandler({
    InvalidPasswordException.class,
    PasswordNotChangedException.class,
    StorageException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.BAD_REQUEST, req);
  }

  // --- 500 (Internal Server Error) ---
  @ExceptionHandler({
    InternalServerErrorException.class,
    UserCreationFailedException.class,
    FileStorageException.class,
    ComplianceRecordNotCreatedException.class
  })
  public ResponseEntity<ErrorResponse> handleInternalError(RuntimeException ex, WebRequest req) {
    return exceptionConfig.processError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
  }
}
