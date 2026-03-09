package com.cts.edusphere.common.dto.Faculty;

import com.cts.edusphere.common.validation.CreateValidation;
import com.cts.edusphere.common.validation.UpdateValidation;
import com.cts.edusphere.enums.Status;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record FacultyRequestDTO(
        @NotBlank(message = "Name cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String name,

        @NotBlank(message = "Email cannot be blank", groups = CreateValidation.class)
        @Email(message = "Email should be valid", groups = {CreateValidation.class, UpdateValidation.class})
        String email,

        @NotBlank(message = "Phone number cannot be blank", groups = CreateValidation.class)
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits", groups = {CreateValidation.class, UpdateValidation.class})
        String phone,

        @NotBlank(message = "Password cannot be blank", groups = CreateValidation.class)
        @Size(min = 8, message = "Password must be at least 8 characters", groups = CreateValidation.class)
        String password,

        @NotBlank(message = "Position cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 2, max = 50, message = "Position must be between 2 and 50 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String position,

        @NotNull(message = "Department ID cannot be null", groups = CreateValidation.class)
        UUID departmentId,

        @NotNull(message = "Status cannot be null", groups = {CreateValidation.class, UpdateValidation.class})
        Status status
) {}
