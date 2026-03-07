package com.cts.edusphere.exceptions;

import com.cts.edusphere.common.responses.ApiResponse;
import com.cts.edusphere.exceptions.genericexceptions.*;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GenericExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(
    GenericExceptionHandler.class
  );

  private ResponseEntity<ErrorResponse> buildErrorResponse(
    String message,
    HttpStatus status,
    WebRequest request,
    Map<String, String> validationError
  ) {
    ErrorResponse response = ErrorResponse.builder()
      .timeStamp(Instant.now())
      .status(status.value())
      .error(status.getReasonPhrase())
      .message(message)
      .path(request.getDescription(false).replace("uri=", ""))
      .validationError(validationError)
      .build();
    return new ResponseEntity<>(response, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobal(
    Exception ex,
    WebRequest request
  ) {
    logger.error("Unhandled exception: {}", ex.getMessage(), ex);
    return buildErrorResponse(
      "An unexpected error occurred",
      HttpStatus.INTERNAL_SERVER_ERROR,
      request,
      null
    );
  }

  @ExceptionHandler(value = ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
    ResourceNotFoundException exception,
    WebRequest request
  ) {
    logger.warn("ResourceNotFoundException: {}", exception.getMessage());
    String message = "Resource not found";
    return buildErrorResponse(message, HttpStatus.NOT_FOUND, request, null);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
    ResourceAlreadyExistsException ex,
    WebRequest request
  ) {
    logger.warn("ResourceAlreadyExistsException: {}", ex.getMessage());
    String message = "Resource already exists";
    return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
    DataIntegrityViolationException ex,
    WebRequest request
  ) {
    logger.warn("DataIntegrityViolationException: {}", ex.getMessage());
    String message =
      "Database integrity violation: Likely a duplicate entry or missing required field.";
    return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(
    InvalidCredentialsException ex,
    WebRequest request
  ) {
    logger.warn("InvalidCredentialsException: {}", ex.getMessage());
    String message = "Invalid credentials provided";
    return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(
    BadCredentialsException ex,
    WebRequest request
  ) {
    logger.warn("BadCredentialsException: {}", ex.getMessage());
    String message = "Invalid credentials provided";
    return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ErrorResponse> handleDisabled(
    DisabledException ex,
    WebRequest request
  ) {
    logger.warn("DisabledException: {}", ex.getMessage());
    String message = "Account is deactivated. Please contact support.";
    return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponse> handleInvalidToken(
    InvalidTokenException ex,
    WebRequest request
  ) {
    logger.warn("InvalidTokenException: {}", ex.getMessage());
    String message = "Invalid authentication token";
    return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<ErrorResponse> handleTokenExpired(
    TokenExpiredException ex,
    WebRequest request
  ) {
    logger.warn("TokenExpiredException: {}", ex.getMessage());
    String message = "Authentication token has expired";
    return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
  }

  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
    UnauthorizedAccessException ex,
    WebRequest request
  ) {
    logger.warn("UnauthorizedAccessException: {}", ex.getMessage());
    String message = "Access is denied";
    return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
  }

  @ExceptionHandler(InsufficientPermissionException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientPermission(
    InsufficientPermissionException ex,
    WebRequest request
  ) {
    logger.warn("InsufficientPermissionException: {}", ex.getMessage());
    String message = "Insufficient permissions to perform this action";
    return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(
    AccessDeniedException ex,
    WebRequest request
  ) {
    logger.warn("AccessDeniedException: {}", ex.getMessage());
    String message = "Access is denied";
    return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
    EmailAlreadyExistsException ex,
    WebRequest request
  ) {
    logger.warn("EmailAlreadyExistsException: {}", ex.getMessage());
    String message = "Email already exists";
    return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPassword(
    InvalidPasswordException ex,
    WebRequest request
  ) {
    logger.warn("InvalidPasswordException: {}", ex.getMessage());
    String message = "Invalid password provided";
    return buildErrorResponse(message, HttpStatus.BAD_REQUEST, request, null);
  }

  @ExceptionHandler(ExternalServiceException.class)
  public ResponseEntity<ErrorResponse> handleExternalServiceException(
    ExternalServiceException ex,
    WebRequest request
  ) {
    logger.error("ExternalServiceException: {}", ex.getMessage());
    String message = "External service error";
    return buildErrorResponse(message, HttpStatus.BAD_GATEWAY, request, null);
  }

  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<ErrorResponse> handleFileStorageException(
    FileStorageException ex,
    WebRequest request
  ) {
    logger.error("FileStorageException: {}", ex.getMessage());
    String message = "File storage error";
    return buildErrorResponse(
      message,
      HttpStatus.INTERNAL_SERVER_ERROR,
      request,
      null
    );
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ErrorResponse> handleInternalServerErrorException(
    InternalServerErrorException ex,
    WebRequest request
  ) {
    logger.error("InternalServerErrorException: {}", ex.getMessage());
    String message = "Internal server error";
    return buildErrorResponse(
      message,
      HttpStatus.INTERNAL_SERVER_ERROR,
      request,
      null
    );
  }

  @ExceptionHandler(PasswordCannotBeChangedException.class)
  public ResponseEntity<ErrorResponse> handlePasswordCannotBeChanged(
    PasswordCannotBeChangedException ex,
    WebRequest request
  ) {
    logger.error("PasswordCannotBeChangedException: {}", ex.getMessage());
    return buildErrorResponse(
      ex.getMessage(),
      HttpStatus.BAD_REQUEST,
      request,
      null
    );
  }

  @ExceptionHandler(UserNotCreatedException.class)
  public ResponseEntity<ErrorResponse> handleUserNotCreated(
    UserNotCreatedException ex,
    WebRequest request
  ) {
    logger.error("UserNotCreatedException: {}", ex.getMessage());
    return buildErrorResponse(
      ex.getMessage(),
      HttpStatus.INTERNAL_SERVER_ERROR,
      request,
      null
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("MethodArgumentNotValidException: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Validation failed",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(DepartmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDepartmentNotFoundException(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("DepartmentNotFoundException: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Fetching department failed",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCourseNotFoundException(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("CourseNotFoundException: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Fetching course by Id failed",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(CourseAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleCourseAlreadyExsistsException(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("CourseAlreadyExistsException: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Course with provided name already exists",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("UserNotFoundException: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "User with provided details doesn't exist",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(NoNotificationFoundWithUserId.class)
  public ResponseEntity<ErrorResponse> handleNoNewNotificationFoundWithId(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("NoNewNotificationFoundWithId: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Notification with provided details of User doesn't exist",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }

  @ExceptionHandler(NoNotificationFoundWithId.class)
  public ResponseEntity<ErrorResponse> handleNoNotificationFoundWithId(
    MethodArgumentNotValidException ex,
    WebRequest request
  ) {
    logger.warn("NoNotificationFoundWithId: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    return buildErrorResponse(
      "Notification with provided details doesn't exist",
      HttpStatus.BAD_REQUEST,
      request,
      errors
    );
  }
}
