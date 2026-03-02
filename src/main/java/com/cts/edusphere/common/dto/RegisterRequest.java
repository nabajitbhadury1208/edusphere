package com.cts.edusphere.common.dto;

import com.cts.edusphere.enums.Role;

import java.util.List;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String phone,
        Role role
) {
}
