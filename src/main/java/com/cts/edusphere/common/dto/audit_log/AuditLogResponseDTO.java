package com.cts.edusphere.common.dto.audit_log;

import java.time.Instant;
import java.util.UUID;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;

/**
 * Data Transfer Object representing a system audit log entry returned from the API.
 * Contains full details of a recorded system event for review or reporting.
 *
 * @param auditLogId the unique identifier of this audit log entry
 * @param userId     the UUID of the user associated with the logged action
 * @param action     the action that was performed (e.g., "CREATE", "DELETE")
 * @param resource   the name or identifier of the resource that was affected
 * @param timestamp  the exact date and time at which the action occurred
 * @param details    additional contextual information about the event
 * @param logType    the system log category classifying the nature of the event
 * @param severity   the severity level of the log entry (INFO, WARN, ERROR, CRITICAL)
 */
public record AuditLogResponseDTO(
        UUID auditLogId,

        UUID userId,

        String action,

        String resource,

        Instant timestamp,

        String details,

        SystemLogType logType,

        Severity severity
) {
}