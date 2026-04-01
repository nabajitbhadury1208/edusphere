package com.cts.edusphere.common.dto.regulator;


import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record RegulatorResponse(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status
) {}

