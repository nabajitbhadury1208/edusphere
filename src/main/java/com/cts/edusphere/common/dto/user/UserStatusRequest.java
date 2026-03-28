package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for changing a user's account activation status.
 * Typically used by administrators to enable or disable user access.
 *
 * @param status the new account status to apply; must not be null (ACTIVE or INACTIVE)
 */
public record UserStatusRequest(
        @NotNull(message = "Status is required")
        Status status
) {
}