package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Data Transfer Object for generating or updating an institutional report.
 *
 * @param metrics      a description of the data or key performance indicators covered by the report;
 *                     must be between 3 and 200 characters; required on create and update
 * @param status       the publication status of the report (ACTIVE or INACTIVE); required on create
 * @param scope        the coverage area of the report (e.g., DEPARTMENT, INSTITUTION, ACADEMIC); required on create
 * @param departmentId the UUID of the department the report pertains to; required on create
 * @param generatedBy  the UUID of the user who initiated the report generation; required on create
 */
public record ReportRequestDto(

        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        @Size(min = 3, max = 200, message = "Metrics must be between 3 and 200 characters")
        String metrics,

        @NotNull(groups = OnCreate.class,message = "Status is required")
        Status status,

        @NotNull(groups = OnCreate.class,message = "Scope is required")
        ReportScope scope,

        @NotNull(groups = OnCreate.class,message = "Department is required")
        UUID departmentId,

        @NotNull(groups = OnCreate.class)
        UUID generatedBy
) {
}
