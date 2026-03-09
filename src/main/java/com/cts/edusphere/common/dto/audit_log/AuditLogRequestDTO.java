package com.cts.edusphere.common.dto.audit_log;

import java.util.UUID;

import com.cts.edusphere.common.validation.CreateValidation;
import com.cts.edusphere.common.validation.UpdateValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuditLogRequestDTO(
        @NotNull(message = "User ID is required", groups = CreateValidation.class)
        UUID userId,

        @NotBlank(message = "Action is required", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 3, max = 100, message = "Action must be between 3 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String action,

        @NotBlank(message = "Resource is required", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 3, max = 100, message = "Resource must be between 3 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String resource
) {}
