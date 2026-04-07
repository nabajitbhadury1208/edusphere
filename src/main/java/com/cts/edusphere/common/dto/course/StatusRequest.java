package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(
    @NotNull(message = "Status is required") Status status) {
}

