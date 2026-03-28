package com.cts.edusphere.common.dto.faculty;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * Data Transfer Object for creating or updating a faculty member's account.
 *
 * @param name         the full name of the faculty member; required on create
 * @param email        the unique email address; must be a valid email format; required on create
 * @param phone        the contact phone number; must be 7 to 15 digits with an optional leading '+'; validated on create and update
 * @param password     the account password; write-only, never returned in responses; required on create
 * @param position     the faculty member's academic position or title (e.g., "Professor"); required on create and update
 * @param departmentId the UUID of the department the faculty member belongs to; required on create
 * @param status       the current account status (ACTIVE or INACTIVE); required on create and update
 */
public record FacultyRequestDTO(
        @NotBlank(groups = OnCreate.class, message = "Name cannot be blank")
        String name,

        @Email(groups = OnCreate.class, message = "Email should be valid")
        @NotBlank(groups = OnCreate.class, message = "Email cannot be blank")
        String email,

        @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits")
        String phone,

        @NotBlank(groups = OnCreate.class, message = "Password cannot be blank")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Position cannot be blank")
        String position,

        @NotNull(groups = OnCreate.class, message = "department ID cannot be null")
        UUID departmentId,

        @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Status cannot be null")
        Status status
) {}