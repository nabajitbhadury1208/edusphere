package com.cts.edusphere.controllers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.AuditEntityType;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<AuditResponseDTO>> getAllAudits() {
        log.info("Fetching all audit records");
        List<AuditResponseDTO> audits = auditService.getAllAudits();
        return ResponseEntity.ok(audits);
    }

    @GetMapping("/by-entity-type")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<AuditResponseDTO>> getAllAuditsByEntityType(@RequestParam AuditEntityType entityType) {

        log.info("Fetching all audit records by entity type");
        return ResponseEntity.ok(auditService.getAuditsByEntityType(entityType));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable UUID id) {
        log.info("Fetching audit record: {}", id);
        AuditResponseDTO responseDTO = auditService.getAuditById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> reviewAudit(@PathVariable UUID id, @Validated(OnUpdate.class) @RequestBody AuditRequestDTO dto) {
        log.info("Reviewing audit record {}", dto);
        return ResponseEntity.ok(auditService.reviewAudit(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAudit(@PathVariable UUID id) {
        log.info("Deleting audit record: {}", id);
        auditService.deleteAudit(id);
        return ResponseEntity.noContent().build();
    }
}