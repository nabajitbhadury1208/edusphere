package com.cts.edusphere.exceptions;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.services.audit_log.AuditLogService;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@RequiredArgsConstructor
@Slf4j
public final class GenericExceptionConfig {
  private final AuditLogService auditLogService;

  public void logSecurityEvent(SystemLogType logType, Severity severity, String details) {
    try {
      auditLogService.logSystemEvent(
          logType, severity, "EXCEPTION_HANDLER", "GenericExceptionHandler", details, null);
    } catch (Exception e) {
      log.error("Failed to write security level audit log {}", e.getMessage());
    }
  }

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

  public ResponseEntity<ErrorResponse> processError(
      Exception ex, HttpStatus status, WebRequest req) {
    SystemLogType logType =
        ExceptionMap.LOG_MAPPINGS.getOrDefault(ex.getClass(), SystemLogType.INTERNAL_ERROR);

    log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    logSecurityEvent(logType, Severity.ERROR, ex.getMessage());

    return buildErrorResponse(ex.getMessage(), status, req, null);
  }
}
