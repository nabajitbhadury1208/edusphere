package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CourseRequest(
    @NotBlank(groups = OnCreate.class, message = "Title must be present") @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters") String title,

    @NotNull(groups = OnCreate.class, message = "DepartmentId is required") UUID departmentId,

    @Min(groups = OnCreate.class, value = 1, message = "Credits must be at least 1") Integer credits,

    @Min(groups = OnCreate.class, value = 1, message = "Duration must be at least 1") Integer duration,

    @NotNull(groups = OnCreate.class, message = "Status is required") Status status) {
}