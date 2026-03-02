package com.cts.edusphere.common.dto;

import java.util.List;

public record RegisterRequest(
        String name,
        String email,
        String password,
        List<String> roles
) {
}
