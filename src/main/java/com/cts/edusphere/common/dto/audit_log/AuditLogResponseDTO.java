package com.cts.edusphere.common.dto.audit_log;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AuditLogResponseDTO(
        @NotNull(message = "Audit Log ID is required") UUID auditLogId,
        @NotNull(message = "User ID is required") UUID userId, @NotNull(message = "Action is required") String action,
        @NotNull(message = "Resource is required") String resource,
        @NotNull(message = "Timestamp is required") String timestamp) {
}
