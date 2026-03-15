package com.cts.edusphere.mappers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.modules.audit.Audit;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class AuditMapper {
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