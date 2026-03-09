package com.cts.edusphere.common.dto.Department;

import com.cts.edusphere.common.validation.CreateValidation;
import com.cts.edusphere.common.validation.UpdateValidation;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.*;

import java.util.UUID;


public record DepartmentRequestDTO(
        @NotBlank(message = "Department name cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String departmentName,

        @NotBlank(message = "Department code cannot be blank", groups = CreateValidation.class)
        @Size(min = 2, max = 20, message = "Department code must be between 2 and 20 characters", groups = {CreateValidation.class, UpdateValidation.class})
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Department code must contain only uppercase letters and numbers", groups = {CreateValidation.class, UpdateValidation.class})
        String departmentCode,

        @NotBlank(message = "Contact info cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 5, max = 100, message = "Contact info must be between 5 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String contactInfo,

        @NotNull(message = "Status cannot be null", groups = {CreateValidation.class, UpdateValidation.class})
        Status status,

        UUID headId
) {}
