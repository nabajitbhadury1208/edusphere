package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.modules.AuditLog;

import java.time.Instant;

import com.cts.edusphere.modules.User;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {
    public AuditLogResponseDTO toResponseDTO(AuditLog entity) {
        return new AuditLogResponseDTO(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getAction(),
                entity.getResource(),
                entity.getCreatedAt()
        );
    }

    public AuditLog toEntity(AuditLogRequestDTO auditLogRequestDTO, User user) {
        return AuditLog
                .builder()
                .user(user)
                .action(auditLogRequestDTO.action())
                .resource(auditLogRequestDTO.resource())
                .build();
    }
}