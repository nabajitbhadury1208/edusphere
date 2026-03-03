package com.cts.edusphere.common.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AuditLogRequestDTO(
        @NotNull(message = "User ID is required") UUID userId,
        @NotNull(message = "Action is required") String action,
        @NotNull(message = "Resource is required") String resource) {
}
