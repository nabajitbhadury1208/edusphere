package com.cts.edusphere.exceptions;

import com.cts.edusphere.exceptions.genericexceptions.ExternalServiceException;
import com.cts.edusphere.exceptions.genericexceptions.FileStorageException;
import com.cts.edusphere.exceptions.genericexceptions.InsufficientPermissionException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.InvalidCredentialsException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceAlreadyExistsException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GenericExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request, Map<String, String> validationError){
        ErrorResponse response = ErrorResponse.builder().timeStamp(Instant.now()).status(status.value()).error(status.getReasonPhrase()).message(message).path(request.getDescription(false).replace("uri=", "")).validationError(validationError).build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
        logger.warn("ResourceNotFoundException: {}", exception.getMessage());
        String message = "Resource not found";
        return buildErrorResponse(message, HttpStatus.NOT_FOUND, request, null);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, WebRequest request) {
        logger.warn("ResourceAlreadyExistsException: {}", ex.getMessage());
        String message = "Resource already exists";
        return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        logger.warn("InvalidCredentialsException: {}", ex.getMessage());
        String message = "Invalid credentials provided";
        return buildErrorResponse(message, HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex, WebRequest request) {
        logger.warn("UnauthorizedAccessException: {}", ex.getMessage());
        String message = "Access is denied";
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPermission(InsufficientPermissionException ex, WebRequest request) {
        logger.warn("InsufficientPermissionException: {}", ex.getMessage());
        String message = "Insufficient permissions to perform this action";
        return buildErrorResponse(message, HttpStatus.FORBIDDEN, request, null);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex, WebRequest request) {
        logger.error("ExternalServiceException: {}", ex.getMessage());
        String message = "External service error";
        return buildErrorResponse(message, HttpStatus.BAD_GATEWAY, request, null);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex, WebRequest request) {
        logger.error("FileStorageException: {}", ex.getMessage());
        String message = "File storage error";
        return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request) {
        logger.error("InternalServerErrorException: {}", ex.getMessage());
        String message = "Internal server error";
        return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, WebRequest request) {
        logger.warn("DataIntegrityViolationException: {}", ex.getMessage());
        String message = "Database conflict: Likely a duplicate roll number or missing required field.";
        return buildErrorResponse(message, HttpStatus.CONFLICT, request, null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("MethodArgumentNotValidException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

}
