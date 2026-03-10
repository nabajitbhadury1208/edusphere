package com.cts.edusphere.common.dto.department;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DepartmentRequestDTO(
        @NotBlank(groups = OnCreate.class, message = "department name cannot be blank")
        String departmentName,

        @NotBlank(groups = OnCreate.class, message = "department code cannot be blank")
        String departmentCode,

        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Contact info cannot be blank")
        String contactInfo,

        @NotNull(groups = OnCreate.class, message = "Status cannot be null")
        Status status,

        // Optional on creation, so no @NotNull here unless required by your business logic
        UUID headId
) {}