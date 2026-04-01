package com.cts.edusphere.common.dto.departmenthead;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record DepartmentHeadResponse(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        Status status
) {}

