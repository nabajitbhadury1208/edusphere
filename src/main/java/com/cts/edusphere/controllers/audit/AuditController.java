package com.cts.edusphere.controllers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
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
/**
 * REST controller for managing compliance audit records.
 * Base path: /api/v1/audits
 * Access is restricted to ADMIN and COMPLIANCE_OFFICER roles unless stated otherwise.
 */
public class AuditController {

    private final AuditService auditService;

    /**
     * Retrieves all audit records in the system.
     * Accessible by ADMIN and COMPLIANCE_OFFICER roles.
     *
     * @return HTTP 200 with a list of all AuditResponseDTO objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<AuditResponseDTO>> getAllAudits() {
        log.info("Fetching all audit records");
        List<AuditResponseDTO> audits = auditService.getAllAudits();
        return ResponseEntity.ok(audits);
    }

    /**
     * Retrieves all audit records filtered by a specific entity type.
     * Accessible by ADMIN and COMPLIANCE_OFFICER roles.
     *
     * @param entityType the AuditEntityType enum value to filter by (e.g., STUDENT_CREATED)
     * @return HTTP 200 with a list of AuditResponseDTO objects matching the given entity type
     */
    @GetMapping("/by-entity-type")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<AuditResponseDTO>> getAllAuditsByEntityType(@RequestParam AuditEntityType entityType) {

        log.info("Fetching all audit records by entity type");
        return ResponseEntity.ok(auditService.getAuditsByEntityType(entityType));
    }

    /**
     * Retrieves a single audit record by its unique identifier.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param id the UUID of the audit record to retrieve
     * @return HTTP 200 with the matching AuditResponseDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable UUID id) {
        log.info("Fetching audit record: {}", id);
        AuditResponseDTO responseDTO = auditService.getAuditById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Reviews and updates an existing audit record with compliance findings.
     * Assigns a compliance officer, records findings, sets audit date to today,
     * and optionally updates the status. Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param id  the UUID of the audit record to review
     * @param dto the request body containing officerId, findings, and optional updated status
     * @return HTTP 200 with the updated AuditResponseDTO
     */
    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<AuditResponseDTO> reviewAudit(@PathVariable UUID id, @Validated(OnUpdate.class) @RequestBody AuditRequestDTO dto) {
        log.info("Reviewing audit record {}", dto);
        return ResponseEntity.ok(auditService.reviewAudit(id, dto));
    }


    /**
     * Permanently deletes an audit record by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the audit record to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAudit(@PathVariable UUID id) {
        log.info("Deleting audit record: {}", id);
        auditService.deleteAudit(id);
        return ResponseEntity.noContent().build();
    }
}