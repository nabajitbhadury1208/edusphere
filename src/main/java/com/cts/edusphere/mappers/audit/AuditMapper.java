package com.cts.edusphere.mappers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.modules.audit.Audit;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Audit} entity objects
 * and their corresponding DTO representations ({@link AuditRequestDTO} and
 * {@link AuditResponseDTO}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever audit
 * mapping is required. New audits are initialised with a {@link AuditStatus#PENDING}
 * status and a {@code null} audit date, both of which are managed by the service layer.</p>
 */
@Component
public class AuditMapper {

    /**
     * Converts an {@link AuditRequestDTO} to an {@link Audit} entity.
     *
     * <p>The audit date is set to {@code null} and the status is defaulted to
     * {@link AuditStatus#PENDING} upon creation. The compliance officer association
     * is not set here and must be assigned separately.</p>
     *
     * @param dto the {@link AuditRequestDTO} containing the data to map
     * @return a new {@link Audit} entity built from the DTO data with a PENDING status
     */
    public Audit toEntity(AuditRequestDTO dto) {
        return Audit.builder()
                .entityType(dto.entityType())
                .entityId(dto.entityId())
                .scope(dto.scope())
                .findings(dto.findings())
                .auditDate(null)
                .status(AuditStatus.PENDING)
                .build();
    }

    /**
     * Converts an {@link Audit} entity to an {@link AuditResponseDTO}.
     *
     * <p>The compliance officer's ID is extracted safely; if no compliance officer
     * is assigned to the audit, the corresponding field in the response will be {@code null}.</p>
     *
     * @param entity the {@link Audit} entity to convert
     * @return an {@link AuditResponseDTO} populated with data from the entity
     */
    public AuditResponseDTO toResponseDTO(Audit entity) {
        return new AuditResponseDTO(
                entity.getId(),
                entity.getComplianceOfficer() != null ? entity.getComplianceOfficer().getId() : null,
                entity.getEntityType(),
                entity.getEntityId(),
                entity.getScope(),
                entity.getFindings(),
                entity.getAuditDate(),
                entity.getStatus()
        );
    }
}
