package com.cts.edusphere.common.dto.compliance_record;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ComplianceRecordRequest(
        @NotNull(groups = {OnCreate.class},message = "Recorded by user ID is required")
        UUID recordedByUserId,

        @NotNull(groups = {OnCreate.class},message = "Entity ID is required")
        UUID entityId,

        @NotBlank(groups = {OnCreate.class},message = "Entity type is required")
        String entityType,

        @NotNull(groups = {OnCreate.class},message = "Compliance type is required")
        ComplianceType complianceType,

        @NotNull(groups = {OnCreate.class},message = "Compliance result is required")
        ComplianceResult result,

        @NotNull(groups = {OnCreate.class},message = "Compliance date is required")
        LocalDate complianceDate,

        String notes
) {

}