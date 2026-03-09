package com.cts.edusphere.services.complianceRecord;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;

import java.util.List;
import java.util.UUID;

public interface ComplianceRecordService {
    ComplianceRecordResponse createRecord(ComplianceRecordRequest request);
    List<ComplianceRecordResponse> getAllRecords();
    ComplianceRecordResponse getRecordById(UUID id);
    ComplianceRecordResponse updateRecord(UUID id, ComplianceRecordRequest request);
    List<ComplianceRecordResponse> getRecordsByEntityId(UUID entityId);
    void deleteRecord(UUID id);
}