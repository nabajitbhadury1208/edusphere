package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.modules.AuditLog;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {
    public AuditLogResponseDTO toResponseDTO(AuditLog entity) {
        return new AuditLogResponseDTO(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getAction(),
                entity.getResource(),
                null,
                entity.getTimestamp()
        );
    }

    public AuditLog toEntity(AuditLogRequestDTO auditLogRequestDTO) {
        return AuditLog
            .builder()
            .id(auditLogRequestDTO.userId())
            .action(auditLogRequestDTO.action())
            .resource(auditLogRequestDTO.resource())
            .timestamp(Instant.now())
            .build();
    }
}