package com.cts.edusphere.common.dto.student;

import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object representing a student account returned from the API.
 *
 * @param id             the unique identifier of the student user account
 * @param name           the full name of the student
 * @param email          the email address associated with the account
 * @param phone          the contact phone number of the student
 * @param roles          the set of roles assigned to this user (typically STUDENT)
 * @param status         the current account status (ACTIVE or INACTIVE)
 * @param dob            the student's date of birth
 * @param gender         the student's gender identity (MALE, FEMALE, or PREFER_NOT_TO_SAY)
 * @param address        the student's residential or mailing address
 * @param enrollmentDate the timestamp when the student was enrolled in the system
 * @param createdAt      the timestamp when the account record was first created
 * @param updatedAt      the timestamp of the most recent update to the account record
 */
public record StudentResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        Set<Role> roles,
        Status status,
        LocalDate dob,
        Gender gender,
        String address,
        Instant enrollmentDate,
        Instant createdAt,
        Instant updatedAt
) {}
