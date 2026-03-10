package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserRequestDto(

        UUID id,
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number should be valid", groups = {OnCreate.class, OnUpdate.class})
        String phone,

        Role role,
        Status status
) {
}
