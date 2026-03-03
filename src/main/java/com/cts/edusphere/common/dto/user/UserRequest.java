package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;

public record UserRequest(
        String name,
        String phone,
        Role role,
        Status status
) {
}
