package com.cts.edusphere.common.dto.student;

import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record StudentResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        Set<Role> roles,
        Status status,
        LocalDate dob,
        Gender gender,
        String address,
        Instant enrollmentDate,
        Instant createdAt,
        Instant updatedAt
) {}
