package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ResearchProjectResponse(
        String title,
        UUID facultyId,
        ProjectStatus status,
        LocalDate endDate,
        LocalDate startDate,
        List<UUID> facultyMembersIdList,
        List<UUID> studentsList
) {
}
