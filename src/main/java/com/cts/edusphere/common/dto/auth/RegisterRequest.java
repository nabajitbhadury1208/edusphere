package com.cts.edusphere.common.dto.auth;

import com.cts.edusphere.enums.Role;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String phone,
        Role role
) {
}
