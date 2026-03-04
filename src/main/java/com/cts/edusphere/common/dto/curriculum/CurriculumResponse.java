package com.cts.edusphere.common.dto.curriculum;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

public record CurriculumResponse(
        UUID id,
        UUID courseId,
        String description,
        String modulesJSON,
        Status status
) {}

