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
                .scope(dto.scope())
                .findings(dto.findings())
                .auditDate(LocalDate.parse(dto.auditDate()))
                .status(AuditStatus.PENDING) // Default status for new audits
                .build();
    }

    public AuditResponseDTO toResponseDTO(Audit entity) {
        return new AuditResponseDTO(
                entity.getId(),
                entity.getComplianceOfficer().getId(),
                entity.getScope(),
                entity.getFindings(),
                entity.getAuditDate(),
                entity.getStatus()
        );
    }
}