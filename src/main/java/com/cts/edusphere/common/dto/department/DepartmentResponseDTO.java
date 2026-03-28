package com.cts.edusphere.common.dto.department;

import com.cts.edusphere.enums.Status;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object representing a department returned from the API.
 *
 * @param id             the unique identifier of the department
 * @param departmentName the full name of the department
 * @param departmentCode a unique short code identifying the department
 * @param contactInfo    contact details for the department
 * @param status         the operational status of the department (ACTIVE or INACTIVE)
 * @param headId         the UUID of the user currently assigned as department head
 * @param headName       the display name of the department head
 * @param createdAt      the timestamp when the department record was first created
 * @param updatedAt      the timestamp of the most recent update to the department record
 */
public record DepartmentResponseDTO(
        UUID id,
        String departmentName,
        String departmentCode,
        String contactInfo,
        Status status,
        UUID headId,
        String headName,
        Instant createdAt,
        Instant updatedAt
) {}
