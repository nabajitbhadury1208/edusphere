package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.modules.ComplianceRecord;
import org.springframework.stereotype.Component;

@Component
public class ComplianceRecordMapper {

    public ComplianceRecordResponse toResponseDto(ComplianceRecord complianceRecord) {
        if (complianceRecord == null) {
            return null;
        }

        return new ComplianceRecordResponse(
                complianceRecord.getId(),
                complianceRecord.getComplianceOfficer() != null ? complianceRecord.getComplianceOfficer().getId() : null,
                complianceRecord.getEntityId(),
                complianceRecord.getEntityType(),
                complianceRecord.getComplianceType(),
                complianceRecord.getResult(),
                complianceRecord.getComplianceDate(),
                complianceRecord.getNotes(),
                complianceRecord.getCreatedAt()
        );
    }

    public ComplianceRecord toEntity(ComplianceRecordRequest request) {
        if (request == null) {
            return null;
        }

        return ComplianceRecord.builder()
                .entityId(request.entityId())
                .entityType(request.entityType())
                .complianceType(request.complianceType())
                .result(request.result())
                .complianceDate(request.complianceDate())
                .notes(request.notes())
                .build();
    }
}
