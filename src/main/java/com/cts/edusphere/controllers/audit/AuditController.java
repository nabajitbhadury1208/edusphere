package com.cts.edusphere.controllers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.audit.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audits")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> create(@Valid @RequestBody AuditRequestDTO dto) {
        try {
            return new ResponseEntity<>(auditService.createAudit(dto), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            log.warn("Compliance officer not found during audit creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Controller error creating audit: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<AuditResponseDTO>> getAll() {
        try {
            return ResponseEntity.ok(auditService.getAllAudits());
        } catch (Exception e) {
            log.error("Controller error fetching all audits: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<AuditResponseDTO> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(auditService.getAuditById(id));
        } catch (ResourceNotFoundException e) {
            log.warn("Audit not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Controller error fetching audit {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody AuditRequestDTO dto) {
        try {
            return ResponseEntity.ok(auditService.updateAudit(id, dto));
        } catch (ResourceNotFoundException e) {
            log.warn("Audit not found for update: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Controller error updating audit {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            auditService.deleteAudit(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Cannot delete. Audit not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Controller error deleting audit {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}