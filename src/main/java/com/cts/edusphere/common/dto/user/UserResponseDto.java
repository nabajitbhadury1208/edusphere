package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status
) {
}
