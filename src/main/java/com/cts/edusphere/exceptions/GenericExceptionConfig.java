package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.services.audit_log.AuditLogService;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

/**
 * Shared helper used by {@link GenericExceptionHandler} to build error responses
 * and record audit-log entries when exceptions are handled.
 *
 * <p>Centralising these two concerns here keeps the handler itself thin and
 * ensures that every handled exception goes through the same logging pipeline
 * regardless of which handler method catches it.</p>
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class GenericExceptionConfig {
  private final AuditLogService auditLogService;

  /**
   * Writes a system-level security or error event to the audit log via
   * {@link AuditLogService}.
   *
   * <p>Failures within this method are silently absorbed and logged at
   * {@code ERROR} level to prevent a logging failure from masking the
   * original exception.</p>
   *
   * @param logType  the {@link SystemLogType} category for the event
   * @param severity the importance level of the event
   * @param details  a human-readable description of what happened
   */
  public void logSecurityEvent(SystemLogType logType, Severity severity, String details) {
    try {
      auditLogService.logSystemEvent(
          logType, severity, "EXCEPTION_HANDLER", "GenericExceptionHandler", details, null);
    } catch (Exception e) {
      log.error("Failed to write security level audit log {}", e.getMessage());
    }
  }

  /**
   * Builds a {@link ResponseEntity} wrapping an {@link ErrorResponse} populated
   * with the current timestamp, HTTP status code, reason phrase, message, request
   * URI, and optional validation-error details.
   *
   * @param message         the human-readable error description to include in the body
   * @param status          the HTTP status code for the response
   * @param request         the current web request, used to extract the request URI
   * @param validationError a map of field names to validation error messages; may
   *                        be {@code null} when no field-level errors are present
   * @return a {@link ResponseEntity} containing the populated {@link ErrorResponse}
   */
  public ResponseEntity<ErrorResponse> buildErrorResponse(
      String message, HttpStatus status, WebRequest request, Map<String, String> validationError) {

    return new ResponseEntity<>(
        ErrorResponse.builder()
            .timeStamp(Instant.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .path(request.getDescription(false).replace("uri=", ""))
            .validationError(validationError)
            .build(),
        status);
  }

  /**
   * Convenience method that looks up the {@link SystemLogType} for the given
   * exception from {@link ExceptionMap#LOG_MAPPINGS}, writes the audit-log entry,
   * and delegates to {@link #buildErrorResponse} to produce the HTTP response.
   *
   * <p>Falls back to {@link SystemLogType#INTERNAL_ERROR} when the exception type
   * is not present in the mapping.</p>
   *
   * @param ex     the exception that was caught
   * @param status the HTTP status code that should be returned to the client
   * @param req    the current web request
   * @return a {@link ResponseEntity} containing the error details
   */
  public ResponseEntity<ErrorResponse> processError(
      Exception ex, HttpStatus status, WebRequest req) {
    SystemLogType logType =
        ExceptionMap.LOG_MAPPINGS.getOrDefault(ex.getClass(), SystemLogType.INTERNAL_ERROR);

    log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    logSecurityEvent(logType, Severity.ERROR, ex.getMessage());

    return buildErrorResponse(ex.getMessage(), status, req, null);
  }
}
