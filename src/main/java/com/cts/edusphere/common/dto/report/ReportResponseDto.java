package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;

import java.util.UUID;

/**
 * Data Transfer Object representing a report record returned from the API.
 *
 * @param id          the unique identifier of the report
 * @param metrics     the description of data or KPIs covered by the report
 * @param status      the publication status of the report (ACTIVE or INACTIVE)
 * @param scope       the coverage area of the report (e.g., DEPARTMENT, INSTITUTION, ACADEMIC)
 * @param department  the UUID of the department this report relates to
 * @param generatedBy the user entity who initiated and generated this report
 */
public record ReportResponseDto(
        UUID id,
        String metrics,
        Status status,
        ReportScope scope,
        UUID department,
        User generatedBy
) {
}
