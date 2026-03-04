package com.cts.edusphere.common.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password must not be) blank")
        String currentPassword,
        @NotBlank(message = "New password must not be blank")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        String newPassword
) {
}
