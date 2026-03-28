package com.cts.edusphere.controllers.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.services.audit_log.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
/**
 * REST controller for querying system audit log entries.
 * Base path: /api/v1/audit-logs
 * All endpoints require ADMIN role at the class level;
 * individual methods may also accept COMPLIANCE role.
 */
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Retrieves all audit log entries in the system.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @return HTTP 200 with a list of all AuditLogResponseDTO objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        List<AuditLogResponseDTO> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    /**
     * Retrieves a specific audit log entry by its unique identifier.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param id the UUID of the audit log entry to retrieve
     * @return HTTP 200 with the matching AuditLogResponseDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditLogResponseDTO> getAuditLogById(@PathVariable UUID id) {
        log.info("Fetching audit log entry: {}", id);
        AuditLogResponseDTO logResponse = auditLogService.getLogById(id);
        return ResponseEntity.ok(logResponse);
    }

    /**
     * Retrieves all audit log entries associated with a specific user.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param userId the UUID of the user whose log entries are to be retrieved
     * @return HTTP 200 with a list of AuditLogResponseDTO objects for the given user
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsByUser(@PathVariable UUID userId) {
        log.info("Fetching audit logs for user: {}", userId);
        List<AuditLogResponseDTO> logs = auditLogService.getLogsByUser(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Retrieves all audit log entries related to a specific resource (case-insensitive partial match).
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param resource the resource name to search for (e.g., "StudentController")
     * @return HTTP 200 with a list of AuditLogResponseDTO objects matching the resource
     */
    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsByResource(@PathVariable String resource) {
        log.info("Fetching audit logs for resource: {}", resource);
        List<AuditLogResponseDTO> logs = auditLogService.getLogsByResource(resource);
        return ResponseEntity.ok(logs);
    }

    /**
     * Retrieves all audit log entries filtered by severity level.
     * Accessible by ADMIN role (class-level restriction).
     *
     * @param severity the Severity enum value to filter by (INFO, WARN, ERROR)
     * @return HTTP 200 with a list of AuditLogResponseDTO objects matching the severity
     */
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsBySeverity(@PathVariable Severity severity) {
        log.info("Fetching system logs by severity {}", severity);
        return ResponseEntity.ok(auditLogService.getLogsBySeverity(severity));
    }

    /**
     * Retrieves all audit log entries filtered by log type.
     * Accessible by ADMIN role (class-level restriction).
     *
     * @param logType the SystemLogType enum value to filter by (e.g., API_ACCESS, INTERNAL_ERROR)
     * @return HTTP 200 with a list of AuditLogResponseDTO objects matching the log type
     */
    @GetMapping("/type/{logType}")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsByType(@PathVariable SystemLogType logType) {
        log.info("Fetching system logs by severity {}", logType);
        return ResponseEntity.ok(auditLogService.getLogsByType(logType));
    }
}