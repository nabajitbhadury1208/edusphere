package com.cts.edusphere.common.dto.compliance_record;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;

import java.time.LocalDate;
import java.util.UUID;

public record ComplianceRecordResponseDTO(UUID complianceId, UUID complianceOfficerId, String entityId,
        String entityType, ComplianceType complianceType, ComplianceResult result, LocalDate complianceDate,
        String notes) {
}
