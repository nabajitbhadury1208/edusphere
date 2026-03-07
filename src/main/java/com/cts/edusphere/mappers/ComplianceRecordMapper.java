package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.modules.ComplianceOfficer;
import com.cts.edusphere.modules.ComplianceRecord;
import org.springframework.stereotype.Component;

@Component
public class ComplianceRecordMapper {

    public ComplianceRecord toEntity(ComplianceRecordRequest dto, ComplianceOfficer officer) {
        if (dto == null) return null;

        return ComplianceRecord.builder()
                .recordedBy(officer)
                .entityId(dto.entityId())
                .entityType(dto.entityType())
                .complianceType(dto.complianceType())
                .result(dto.result())
                .complianceDate(dto.complianceDate())
                .notes(dto.notes())
                .build();
    }

    public ComplianceRecordResponse toResponse(ComplianceRecord record) {
        if (record == null) return null;

        return new ComplianceRecordResponse(
                record.getId(),
                record.getRecordedBy().getId(),
                record.getEntityId(),
                record.getEntityType(),
                record.getComplianceType(),
                record.getResult(),
                record.getComplianceDate(),
                record.getNotes(),
                record.getCreatedAt()
        );
    }
}