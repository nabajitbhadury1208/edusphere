package com.cts.edusphere.common.dto.audit_log;

import java.time.Instant;
import java.util.UUID;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import jakarta.validation.constraints.NotNull;

public record AuditLogResponseDTO(
        UUID auditLogId,

        UUID userId,

        String action,

        String resource,

        Instant timestamp,

        String details,

        SystemLogType logType,

        Severity severity
) {
}