package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record AuditRequestDTO(
        @NotNull(groups = OnCreate.class, message = "Officer ID is required")
        UUID officerId,

        @NotBlank(groups = OnCreate.class, message = "Scope cannot be blank")
        String scope,
        //TODO NB SCOPE IS ALSO AS STRING

        @NotBlank(groups = OnCreate.class, message = "Findings cannot be blank")
        String findings,

        @NotBlank(groups = OnCreate.class, message = "Audit Date is required")
        @Pattern(
                groups = {OnCreate.class, OnUpdate.class},
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Audit date must be in the format YYYY-MM-DD"
        )
        String auditDate
        //TODO NB PLEASE CHECK THIS DATE IS USED AS STRING
) {}