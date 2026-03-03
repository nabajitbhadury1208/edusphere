package com.cts.edusphere.common.dto;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ComplianceRecordRequestDTO(
        @NotNull(message = "Compliance Officer ID is required") UUID complianceOfficerId,
        @NotNull(message = "Entity ID is required") UUID entityId,
        @NotNull(message = "Entity Type is required") String entityType,
        @NotNull(message = "Compliance Type is required") ComplianceType complianceType,
        @NotNull(message = "Compliance Result is required") ComplianceResult result,
        @NotNull(message = "Notes are required") String notes) {
}
