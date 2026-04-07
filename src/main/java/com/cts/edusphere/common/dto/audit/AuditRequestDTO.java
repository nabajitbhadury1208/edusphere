package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.AuditStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuditRequestDTO(

        @NotNull(groups = OnCreate.class, message = "Entity type is required")
        AuditEntityType entityType,

        UUID entityId,

        @NotBlank(groups = OnCreate.class, message = "Scope cannot be blank")
        String scope,

        String findings,

        AuditStatus status
) {}