package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number should be valid")
        String phone,
        Role role,
        Status status
) {
}
