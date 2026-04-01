package com.cts.edusphere.common.dto.course;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        String title,
        UUID departmentId,
        String departmentName,
        Integer credits,
        Integer duration,
        Status status
) {}
