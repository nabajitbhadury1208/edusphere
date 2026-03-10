package com.cts.edusphere.common.dto.department;

import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.util.UUID;

public record DepartmentResponseDTO(
        UUID id,
        String departmentName,
        String departmentCode,
        String contactInfo,
        Status status,
        UUID headId,
        String headName,
        Instant createdAt,
        Instant updatedAt
) {}
