package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a research project returned from the API.
 *
 * @param projectID            the unique identifier of the research project
 * @param title                the title of the research project
 * @param facultyId            the UUID of the lead faculty member for this project
 * @param status               the current lifecycle status of the project (ACTIVE, ON_HOLD, COMPLETED, CANCELLED)
 * @param endDate              the projected or actual end date of the project
 * @param startDate            the date on which the project started
 * @param facultyMembersIdList the list of UUIDs for additional faculty contributors on the project
 * @param studentsList         the list of UUIDs for students participating in the project
 */
public record ResearchProjectResponse(
        UUID projectID,
        String title,
        UUID facultyId,
        ProjectStatus status,
        LocalDate endDate,
        LocalDate startDate,
        List<UUID> facultyMembersIdList,
        List<UUID> studentsList
) {
}
