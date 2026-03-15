package com.cts.edusphere.common.dto.audit_log;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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