package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.AuditStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object representing the response payload for an audit record.
 * Returned when querying or creating audit entries.
 *
 * @param auditId    the unique identifier of the audit record
 * @param officerId  the UUID of the audit officer who conducted the audit
 * @param entityType the category of the audited entity (e.g., STUDENT, DEPARTMENT)
 * @param entityId   the UUID of the specific entity instance that was audited
 * @param scope      a description of the audit coverage or focus area
 * @param findings   a summary of the observations or issues found during the audit
 * @param auditDate  the date on which the audit was performed
 * @param status     the current status of the audit (e.g., PENDING, COMPLETED, FLAGGED)
 */
public record AuditResponseDTO(
        UUID auditId,
        UUID officerId,
        AuditEntityType entityType,
        UUID entityId,
        String scope,
        String findings,
        LocalDate auditDate,
        AuditStatus status
) {
}
