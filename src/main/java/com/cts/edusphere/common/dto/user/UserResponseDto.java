package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object representing a generic user account returned from the API.
 *
 * @param id     the unique identifier of the user account
 * @param name   the display name of the user
 * @param email  the email address associated with the account
 * @param phone  the contact phone number of the user
 * @param roles  the set of roles assigned to this user (e.g., STUDENT, FACULTY, ADMIN)
 * @param status the current account status (ACTIVE or INACTIVE)
 */
public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String phone,
        Set<Role> roles,
        Status status
) {
}
