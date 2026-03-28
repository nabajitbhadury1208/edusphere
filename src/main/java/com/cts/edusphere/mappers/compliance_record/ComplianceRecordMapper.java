package com.cts.edusphere.mappers.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.modules.compliance_record.ComplianceRecord;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link ComplianceRecord} entity objects
 * and their corresponding DTO representations ({@link ComplianceRecordRequest} and
 * {@link ComplianceRecordResponse}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever compliance record
 * mapping is required.</p>
 */
@Component
public class ComplianceRecordMapper {

    /**
     * Converts a {@link ComplianceRecord} entity to a {@link ComplianceRecordResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The compliance officer's ID is extracted safely; if the officer is {@code null},
     * the corresponding field in the response will be {@code null}.</p>
     *
     * @param complianceRecord the {@link ComplianceRecord} entity to convert; may be {@code null}
     * @return a {@link ComplianceRecordResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
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

    /**
     * Converts a {@link ComplianceRecordRequest} DTO to a {@link ComplianceRecord} entity.
     *
     * <p>If the provided request is {@code null}, this method returns {@code null}.
     * Note that the compliance officer association is not set here and must be assigned
     * separately after entity creation.</p>
     *
     * @param request the {@link ComplianceRecordRequest} DTO containing the data to map;
     *                may be {@code null}
     * @return a new {@link ComplianceRecord} entity built from the request data,
     *         or {@code null} if the input is {@code null}
     */
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
