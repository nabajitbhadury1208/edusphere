package com.cts.edusphere.common.dto.compliance_record;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ComplianceRecordRequest(
        @NotNull(message = "Recorded by user ID is required")
        UUID recordedByUserId,

        @NotNull(message = "Entity ID is required")
        UUID entityId,

        @NotBlank(message = "Entity type is required")
        String entityType,

        @NotNull(message = "Compliance type is required")
        ComplianceType complianceType,

        @NotNull(message = "Compliance result is required")
        ComplianceResult result,

        @NotNull(message = "Compliance date is required")
        LocalDate complianceDate,

        String notes
) {

}