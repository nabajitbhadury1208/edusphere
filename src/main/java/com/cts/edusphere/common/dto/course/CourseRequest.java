package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Data Transfer Object for creating or updating a course.
 *
 * @param title        the display name of the course; must be between 3 and 200 characters; required on create
 * @param departmentId the UUID of the department that owns this course; required on create
 * @param credits      the number of academic credits awarded upon completion; must be at least 1; required on create
 * @param duration     the duration of the course in weeks or hours; must be at least 1; required on create
 * @param status       the current availability status of the course (ACTIVE or INACTIVE); required on create
 */
public record CourseRequest(
    @NotBlank(groups = OnCreate.class, message = "Title must be present") @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters") String title,

    @NotNull(groups = OnCreate.class, message = "DepartmentId is required") UUID departmentId,

    @Min(groups = OnCreate.class, value = 1, message = "Credits must be at least 1") Integer credits,

    @Min(groups = OnCreate.class, value = 1, message = "Duration must be at least 1") Integer duration,

    @NotNull(groups = OnCreate.class, message = "Status is required") Status status) {
}