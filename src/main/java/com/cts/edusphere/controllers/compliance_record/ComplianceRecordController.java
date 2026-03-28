package com.cts.edusphere.controllers.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.compliance_record.ComplianceRecordService;
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
@RequestMapping("/api/v1/compliance-records")
@RequiredArgsConstructor
@Slf4j
/**
 * REST controller for managing compliance records.
 * Base path: /api/v1/compliance-records
 */
public class ComplianceRecordController {

    private final ComplianceRecordService complianceRecordService;

    /**
     * Creates a new compliance record in the system.
     * Accessible by ADMIN and COMPLIANCE_OFFICER roles.
     *
     * @param request the request containing officer ID, entity ID/type, compliance type, result, and notes
     * @return HTTP 201 with the created ComplianceRecordResponse
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<ComplianceRecordResponse> createComplianceRecord(@Validated(OnCreate.class) @RequestBody ComplianceRecordRequest request) {
        ComplianceRecordResponse response = complianceRecordService.createComplianceRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all compliance records in the system.
     * Accessible by ADMIN, COMPLIANCE_OFFICER, and DEPARTMENT_HEAD roles.
     *
     * @return HTTP 200 with a list of all ComplianceRecordResponse objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<ComplianceRecordResponse>> getAllComplianceRecords() {
        return ResponseEntity.ok(complianceRecordService.getAllComplianceRecords());
    }

    /**
     * Retrieves a specific compliance record by its unique identifier.
     * Accessible by ADMIN, COMPLIANCE_OFFICER, and DEPARTMENT_HEAD roles.
     *
     * @param id the UUID of the compliance record to retrieve
     * @return HTTP 200 with the matching ComplianceRecordResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<ComplianceRecordResponse> getComplianceRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordById(id));
    }

    /**
     * Retrieves all compliance records associated with a specific entity.
     * Accessible by ADMIN, COMPLIANCE_OFFICER, and DEPARTMENT_HEAD roles.
     *
     * @param entityId the UUID of the entity whose compliance records are to be fetched
     * @return HTTP 200 with a list of ComplianceRecordResponse objects for the given entity
     */
    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<ComplianceRecordResponse>> getComplianceRecordsByEntityId(@PathVariable UUID entityId) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordsByEntityId(entityId));
    }

    /**
     * Retrieves all compliance records associated with a specific compliance officer user.
     * Accessible by ADMIN and COMPLIANCE_OFFICER roles.
     *
     * @param userId the UUID of the compliance officer user
     * @return HTTP 200 with a list of ComplianceRecordResponse objects for the given officer
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<ComplianceRecordResponse>> getComplianceRecordsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordsByUserId(userId));
    }

    /**
     * Updates an existing compliance record by its unique identifier.
     * Supports partial updates: only non-null fields are applied.
     * Accessible by ADMIN and COMPLIANCE_OFFICER roles.
     *
     * @param id      the UUID of the compliance record to update
     * @param request the request body containing updated fields
     * @return HTTP 200 with a success confirmation message
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<String> updateComplianceRecordById(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody ComplianceRecordRequest request) {
        complianceRecordService.updateComplianceRecord(id, request);
        return ResponseEntity.ok("Successfully updated ComplianceRecord with id: " + id);
    }

    /**
     * Permanently deletes a compliance record by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the compliance record to delete
     * @return HTTP 200 with a success confirmation message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteComplianceRecordById(@PathVariable UUID id) {
        complianceRecordService.deleteComplianceRecordById(id);
        return ResponseEntity.ok("Successfully deleted ComplianceRecord with id: " + id);
    }
}
