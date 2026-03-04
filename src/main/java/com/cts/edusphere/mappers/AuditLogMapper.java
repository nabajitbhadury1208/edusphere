package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.modules.AuditLog;
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
}