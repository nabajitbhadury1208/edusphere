package com.cts.edusphere.common.dto.faculty;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.util.UUID;

public record FacultyResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status,
        String position,
        UUID departmentId,
        String departmentName,
        Instant joinDate,
        Instant createdAt,
        Instant updatedAt
) {}
