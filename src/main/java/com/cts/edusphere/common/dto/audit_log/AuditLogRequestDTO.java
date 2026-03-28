package com.cts.edusphere.common.dto.audit_log;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * Data Transfer Object for submitting a new system audit log entry.
 * Captures metadata about an action performed within the application.
 *
 * @param userId   the UUID of the user who performed the action; may be null for system-initiated events
 * @param action   a short description of the action taken (e.g., "CREATE", "DELETE")
 * @param resource the name or identifier of the resource that was acted upon
 * @param logType  the category of the log entry, indicating what kind of system event occurred
 * @param severity the importance level of this log entry (INFO, WARN, ERROR, CRITICAL)
 * @param details  additional contextual information about the action or event
 */
@Builder
public record AuditLogRequestDTO(
        UUID userId,

        String action,

        String resource,

        SystemLogType logType,

        Severity severity,

        String details
) {
}