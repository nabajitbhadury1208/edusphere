package com.cts.edusphere.common.dto.audit_log;

import com.cts.edusphere.common.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuditLogRequestDTO(
        @NotNull(groups = OnCreate.class, message = "User ID is required")
        UUID userId,

        @NotBlank(groups = OnCreate.class, message = "Action is required")
        String action,

        @NotBlank(groups = OnCreate.class, message = "Resource is required")
        String resource
) {
}