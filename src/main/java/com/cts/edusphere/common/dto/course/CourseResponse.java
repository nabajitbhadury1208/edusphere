package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

/**
 * Data Transfer Object representing a course returned from the API.
 *
 * @param id             the unique identifier of the course
 * @param title          the display name of the course
 * @param departmentId   the UUID of the department that owns this course
 * @param departmentName the human-readable name of the owning department
 * @param credits        the number of academic credits the course is worth
 * @param duration       the duration of the course in weeks or hours
 * @param status         the current availability status of the course (ACTIVE or INACTIVE)
 */
public record CourseResponse(
        UUID id,
        String title,
        UUID departmentId,
        String departmentName,
        Integer credits,
        Integer duration,
        Status status
) {}
