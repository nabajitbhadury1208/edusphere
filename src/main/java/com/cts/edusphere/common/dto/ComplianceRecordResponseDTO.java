package com.cts.edusphere.common.dto;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ComplianceRecordResponseDTO(@NotNull(message = "Compliance ID is required") UUID complianceId,
        @NotNull(message = "Compliance Officer ID is required") UUID complianceOfficerId,
        @NotNull(message = "Entity ID is required") String entityId,
        @NotNull(message = "Entity Type is required") String entityType,
        @NotNull(message = "Compliance Type is required") ComplianceType complianceType,
        @NotNull(message = "Compliance Result is required") ComplianceResult result,
        @NotNull(message = "Compliance Date is required") LocalDate complianceDate,
        @NotNull(message = "Notes are required") String notes
) {
}
