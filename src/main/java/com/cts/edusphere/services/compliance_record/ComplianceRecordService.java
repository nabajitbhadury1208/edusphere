package com.cts.edusphere.services.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;

import java.util.List;
import java.util.UUID;

public interface ComplianceRecordService {
    ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request);
    List<ComplianceRecordResponse> getAllComplianceRecords();
    ComplianceRecordResponse getComplianceRecordById(UUID id);
    List<ComplianceRecordResponse> getComplianceRecordsByEntityId(UUID entityId);
    List<ComplianceRecordResponse> getComplianceRecordsByUserId(UUID userId);
    void updateComplianceRecord(UUID id, ComplianceRecordRequest request);
    void deleteComplianceRecordById(UUID id);
}