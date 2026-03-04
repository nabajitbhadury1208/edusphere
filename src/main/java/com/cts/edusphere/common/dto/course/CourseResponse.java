package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record CourseResponse(
        String title,
        UUID departmentId,
        String departmentName,
        int credits,
        int duration,
        Status status
) {}

