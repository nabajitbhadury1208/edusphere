package com.cts.edusphere.common.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user login credentials.
 * Submitted by a client to obtain authentication tokens.
 *
 * @param email    the registered email address of the user; must be a valid email format
 * @param password the user's account password; write-only and never returned in responses
 */
public record LoginRequest(
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email should be valid")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password must not be blank")
        String password
) {
}
