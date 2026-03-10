package com.cts.edusphere.controllers.compliancerecord;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.complianceRecord.ComplianceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ComplianceRecordController {

    private final ComplianceRecordService recordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ComplianceRecordResponse> create(@Validated(OnCreate.class) @RequestBody ComplianceRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.createRecord(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ComplianceRecordResponse>> getAll() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<ComplianceRecordResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ComplianceRecordResponse>> getByEntity(@PathVariable UUID entityId) {
        return ResponseEntity.ok(recordService.getRecordsByEntityId(entityId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ComplianceRecordResponse> update(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody ComplianceRecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}