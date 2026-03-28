package com.cts.edusphere.common.dto.departmenthead;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.UUID;

/**
 * Data Transfer Object representing a department head user returned from the API.
 *
 * @param id     the unique identifier of the department head user account
 * @param name   the full name of the department head
 * @param email  the email address associated with this account
 * @param phone  the contact phone number of the department head
 * @param role   the role assigned to this user (typically DEPARTMENT_HEAD)
 * @param status the current account status (ACTIVE or INACTIVE)
 */
public record DepartmentHeadResponse(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status
) {}

