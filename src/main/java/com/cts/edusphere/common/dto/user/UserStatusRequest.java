package com.cts.edusphere.common.dto.user;

import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UserStatusRequest(
        @NotNull(message = "Status is required")
        Status status
) {
}