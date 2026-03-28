package com.cts.edusphere.common.dto.regulator;


import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.UUID;

/**
 * Data Transfer Object representing a regulator user returned from the API.
 *
 * @param id     the unique identifier of the regulator user account
 * @param name   the full name of the regulator
 * @param email  the email address associated with the account
 * @param phone  the contact phone number of the regulator
 * @param role   the role assigned to this user (typically REGULATOR)
 * @param status the current account status (ACTIVE or INACTIVE)
 */
public record RegulatorResponse(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status
) {}

