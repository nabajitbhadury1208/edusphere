package com.cts.edusphere.controllers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.audit.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<AuditResponseDTO> createAudit(@Validated(OnCreate.class) @RequestBody AuditRequestDTO dto) {
        log.info("Creating new audit record for officer: {}", dto.officerId());
        AuditResponseDTO responseDTO = auditService.createAudit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<AuditResponseDTO>> getAllAudits() {
        log.info("Fetching all audit records");
        List<AuditResponseDTO> audits = auditService.getAllAudits();
        return ResponseEntity.ok(audits);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable UUID id) {
        log.info("Fetching audit record: {}", id);
        AuditResponseDTO responseDTO = auditService.getAuditById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> updateAudit(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody AuditRequestDTO dto) {
        log.info("Updating audit record: {}", id);
        AuditResponseDTO responseDTO = auditService.updateAudit(id, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAudit(@PathVariable UUID id) {
        log.info("Deleting audit record: {}", id);
        auditService.deleteAudit(id);
        return ResponseEntity.noContent().build();
    }
}