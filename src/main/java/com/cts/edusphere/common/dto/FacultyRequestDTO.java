package com.cts.edusphere.common.dto;

import com.cts.edusphere.enums.Status;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record FacultyRequestDTO(
        @NotBlank(message = "Name cannot be blank") String name,
        @Email(message = "Email should be valid") @NotBlank(message = "Email cannot be blank") String email,
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits") String phone,
        @NotBlank(message = "Password cannot be blank") String password,
        @NotBlank(message = "Position cannot be blank") String position,
        @NotNull(message = "Department ID cannot be null") UUID departmentId,
        @NotNull(message = "Status cannot be null") Status status
) {}
