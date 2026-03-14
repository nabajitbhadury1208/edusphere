package com.cts.edusphere.common.dto.curriculum;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

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