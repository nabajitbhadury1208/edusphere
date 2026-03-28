package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object for updating an existing user's general profile information.
 * Email and password changes are handled through dedicated endpoints.
 *
 * @param id     the UUID of the user to be updated; used for identification purposes
 * @param name   the updated display name; must be between 3 and 50 characters; validated on create and update
 * @param phone  the updated phone number; must match a 7-15 digit pattern with an optional leading '+'; validated on create and update
 * @param roles  the updated set of roles assigned to this user
 * @param status the updated account status (ACTIVE or INACTIVE)
 */
public record UserRequestDto(

        UUID id,
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number should be valid", groups = {OnCreate.class, OnUpdate.class})
        String phone,


        Set<Role> roles,

        Status status
) {
}
