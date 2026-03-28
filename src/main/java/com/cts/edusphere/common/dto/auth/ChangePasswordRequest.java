package com.cts.edusphere.common.dto.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for a user's password change request.
 * Both fields are write-only and will never be serialized in API responses.
 *
 * @param currentPassword the user's existing password used to verify their identity before the change
 * @param newPassword     the desired new password; must be at least 8 characters long
 */
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
