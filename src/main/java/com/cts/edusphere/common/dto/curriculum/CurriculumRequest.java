package com.cts.edusphere.common.dto.curriculum;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Data Transfer Object for creating or updating a curriculum entry for a course.
 *
 * @param courseId     the UUID of the course this curriculum belongs to; required on create
 * @param description  a textual overview of the curriculum content; must be between 3 and 1000 characters; required on create
 * @param modulesJSON  a JSON string encoding the structured list of modules in this curriculum; optional
 * @param status       the publish status of the curriculum (ACTIVE or INACTIVE); required on create
 */
public record CurriculumRequest(
        @NotNull(groups = {OnCreate.class}, message = "CourseId is required")
        UUID courseId,

        @NotBlank(groups = {OnCreate.class})
        @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
        String description,


        String modulesJSON,

        @NotNull(message = "Status is required", groups = {OnCreate.class})
        Status status
) {}