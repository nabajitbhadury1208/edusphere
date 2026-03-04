package com.cts.edusphere.common.dto.researchproject;

import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.modules.Faculty;

import java.time.LocalDate;

public record ResearchProjectResponse(
        String title,
        Faculty faculty,
        ProjectStatus status,
        LocalDate endDate,
        LocalDate startDate
) {
}
