package com.cts.edusphere.common.dto.auth;

import com.cts.edusphere.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name must not be blank") String name,

        @NotBlank(message = "Email must not be blank") @Email(message = "Email should be valid") String email,

        @NotBlank(message = "Password must not be blank") @Size(min = 8, message = "Password must be at least 8 characters long") String password,

        String phone,

        @NotNull(message = "Role must not be null")
        Role role) {
}
