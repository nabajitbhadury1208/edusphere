package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Data Transfer Object for assigning or updating a faculty member's teaching workload.
 *
 * @param facultyId the UUID of the faculty member whose workload is being recorded; required on create
 * @param courseId  the UUID of the course assigned to this workload entry; required on create
 * @param hours     the number of teaching hours assigned per week or semester; optional
 * @param semester  the academic semester identifier for which this workload applies (e.g., "Fall 2025"); required on create
 * @param status    the current status of this workload assignment (ACTIVE or INACTIVE); required on create
 */
public record WorkLoadRequestDto(

        @NotNull(groups = OnCreate.class,message = "Faculty id is required")
        UUID facultyId,

        @NotNull(groups = OnCreate.class,message = "Course id is required")
        UUID courseId,

        Integer hours,

        @NotNull(groups = OnCreate.class,message = "Semester is required")
        String semester,

        @NotNull(groups = OnCreate.class,message = "Status is required")
        Status status
) {
}
