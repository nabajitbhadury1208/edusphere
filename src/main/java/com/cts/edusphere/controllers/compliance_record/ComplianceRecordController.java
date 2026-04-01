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
public class ComplianceRecordController {

    private final ComplianceRecordService complianceRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<ComplianceRecordResponse> createComplianceRecord(@Validated(OnCreate.class) @RequestBody ComplianceRecordRequest request) {
        ComplianceRecordResponse response = complianceRecordService.createComplianceRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<ComplianceRecordResponse>> getAllComplianceRecords() {
        return ResponseEntity.ok(complianceRecordService.getAllComplianceRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<ComplianceRecordResponse> getComplianceRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordById(id));
    }

    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<ComplianceRecordResponse>> getComplianceRecordsByEntityId(@PathVariable UUID entityId) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordsByEntityId(entityId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<ComplianceRecordResponse>> getComplianceRecordsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(complianceRecordService.getComplianceRecordsByUserId(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<String> updateComplianceRecordById(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody ComplianceRecordRequest request) {
        complianceRecordService.updateComplianceRecord(id, request);
        return ResponseEntity.ok("Successfully updated ComplianceRecord with id: " + id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteComplianceRecordById(@PathVariable UUID id) {
        complianceRecordService.deleteComplianceRecordById(id);
        return ResponseEntity.ok("Successfully deleted ComplianceRecord with id: " + id);
    }
}
