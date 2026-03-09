package com.cts.edusphere.common.dto.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Current password must not be) blank")
        String currentPassword,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "New password must not be blank")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        String newPassword
) {
}
