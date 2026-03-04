package com.cts.edusphere.controllers.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.auditLog.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<AuditLogResponseDTO>> getAll() {
        try {
            return ResponseEntity.ok(auditLogService.getAllLogs());
        } catch (Exception e) {
            log.error("Error fetching all audit logs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditLogResponseDTO> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(auditLogService.getLogById(id));
        } catch (ResourceNotFoundException e) {
            log.warn("Audit log entry not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching audit log with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getByUser(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(auditLogService.getLogsByUser(userId));
        } catch (Exception e) {
            log.error("Error fetching audit logs for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<List<AuditLogResponseDTO>> getByResource(@PathVariable String resource) {
        try {
            return ResponseEntity.ok(auditLogService.getLogsByResource(resource));
        } catch (Exception e) {
            log.error("Error fetching audit logs for resource {}: {}", resource, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}