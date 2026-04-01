package com.cts.edusphere.common.dto.audit_log;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AuditLogRequestDTO(
        UUID userId,

        String action,

        String resource,

        SystemLogType logType,

        Severity severity,

        String details
) {
}