package com.cts.edusphere.common.dto.faculty;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object representing a faculty member returned from the API.
 *
 * @param id             the unique identifier of the faculty user account
 * @param name           the full name of the faculty member
 * @param email          the email address associated with the account
 * @param phone          the contact phone number of the faculty member
 * @param roles          the set of roles assigned to this user (e.g., FACULTY)
 * @param status         the current account status (ACTIVE or INACTIVE)
 * @param position       the academic position or title held by the faculty member
 * @param departmentId   the UUID of the department this faculty member is affiliated with
 * @param departmentName the human-readable name of the affiliated department
 * @param joinDate       the timestamp when the faculty member joined the institution
 * @param createdAt      the timestamp when the account record was first created
 * @param updatedAt      the timestamp of the most recent update to the account record
 */
public record FacultyResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        Set<Role> roles,
        Status status,
        String position,
        UUID departmentId,
        String departmentName,
        Instant joinDate,
        Instant createdAt,
        Instant updatedAt
) {
}
