package com.cts.edusphere.controllers.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.services.auditLog.AuditLogService;
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
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        List<AuditLogResponseDTO> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditLogResponseDTO> getAuditLogById(@PathVariable UUID id) {
        log.info("Fetching audit log entry: {}", id);
        AuditLogResponseDTO logResponse = auditLogService.getLogById(id);
        return ResponseEntity.ok(logResponse);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsByUser(@PathVariable UUID userId) {
        log.info("Fetching audit logs for user: {}", userId);
        List<AuditLogResponseDTO> logs = auditLogService.getLogsByUser(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditLogsByResource(@PathVariable String resource) {
        log.info("Fetching audit logs for resource: {}", resource);
        List<AuditLogResponseDTO> logs = auditLogService.getLogsByResource(resource);
        return ResponseEntity.ok(logs);
    }
}