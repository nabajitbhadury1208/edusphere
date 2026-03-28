package com.cts.edusphere.common.dto.compliance_record;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for creating a new compliance record.
 * Captures all information needed to log a compliance evaluation for a given entity.
 *
 * @param recordedByUserId the UUID of the user (e.g., compliance officer) who recorded this entry; required on create
 * @param entityId         the UUID of the entity (e.g., student, faculty) being evaluated; required on create
 * @param entityType       a string identifier for the type of entity being evaluated; required on create
 * @param complianceType   the domain area of compliance being checked (e.g., COURSE, EXAM, RESEARCH); required on create
 * @param result           the outcome of the compliance check (e.g., PASS, FAIL, PENDING); required on create
 * @param complianceDate   the date on which the compliance evaluation was carried out; required on create
 * @param notes            optional free-text remarks or explanations regarding the compliance outcome
 */
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