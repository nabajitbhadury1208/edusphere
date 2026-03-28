package com.cts.edusphere.common.dto.regulator;

import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object for creating a regulator user account.
 * Regulators are external oversight users who monitor institutional compliance.
 *
 * @param name     the full name of the regulator; must not be blank
 * @param email    the regulator's unique email address; must be a valid email format
 * @param phone    the contact phone number; must be 7 to 15 digits with an optional leading '+'
 * @param password the account password; write-only and never returned in responses
 * @param status   the initial account status (ACTIVE or INACTIVE)
 */
public record RegulatorRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits")
        String phone,

        @NotBlank(message = "Password cannot be blank")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @NotNull(message = "Status cannot be null")
        Status status
) {}
