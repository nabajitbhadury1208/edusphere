package com.cts.edusphere.common.dto.compliance_record;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object representing a compliance record returned from the API.
 * Provides the full details of a stored compliance evaluation entry.
 *
 * @param id                 the unique identifier of this compliance record
 * @param recordedByUserId   the UUID of the user who recorded this compliance entry
 * @param entityId           the UUID of the entity that was evaluated for compliance
 * @param entityType         the string type descriptor of the evaluated entity
 * @param complianceType     the domain area that was assessed (e.g., COURSE, EXAM, RESEARCH)
 * @param result             the outcome of the compliance evaluation (e.g., PASS, FAIL)
 * @param complianceDate     the date on which the compliance evaluation took place
 * @param notes              any additional remarks or context associated with this record
 * @param createdAt          the timestamp when this record was first persisted in the system
 */
public record ComplianceRecordResponse(
        UUID id,
        UUID recordedByUserId,
        UUID entityId,
        String entityType,
        ComplianceType complianceType,
        ComplianceResult result,
        LocalDate complianceDate,
        String notes,
        Instant createdAt
) {}