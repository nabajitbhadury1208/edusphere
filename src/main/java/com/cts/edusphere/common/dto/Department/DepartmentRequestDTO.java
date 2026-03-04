package com.cts.edusphere.common.dto.Department;

import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.*;

import java.util.UUID;


public record DepartmentRequestDTO(
        @NotBlank(message = "Department name cannot be blank") String departmentName,
        @NotBlank(message = "Department code cannot be blank") String departmentCode,
        @NotBlank(message = "Contact info cannot be blank") String contactInfo,
        @NotNull(message = "Status cannot be null") Status status,
        UUID headId
) {}
