package com.cts.edusphere.common.dto.audit_log;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AuditLogResponseDTO(
        UUID auditLogId,
        UUID userId,
        String action,
        String resource,
        String timestamp) {}
