package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for creating a new research project.
 *
 * @param title          the title of the research project; must be between 3 and 100 characters
 * @param facultyId      the UUID of the lead faculty member responsible for the project; required
 * @param facultyMembers a list of UUIDs for additional faculty contributors; send an empty list if none; required
 * @param students       a list of UUIDs for students participating in the project; send an empty list if none; required
 * @param startDate      the date on which the research project begins; required
 * @param endDate        the projected or actual end date of the research project; required
 * @param status         the current lifecycle status of the project (ACTIVE, ON_HOLD, COMPLETED, CANCELLED); required
 */
public record ResearchProjectRequest(

        @NotBlank
        @Size(min = 3, max = 100, message = "Title must be between 3 and 200 characters")
        String title,

        @NotNull(message = "Faculty is required")
        UUID facultyId,

        @NotNull(message = "facultyMember cannot be null; send empty list if none")
        List<UUID> facultyMembers,

        @NotNull(message = "student cannot be null; send empty list if none")
        List<UUID> students,

        @NotNull(message = "startDate is required")
        LocalDate startDate,

        @NotNull(message = "endDate is required")
        LocalDate endDate,

        @NotNull(message = "status is required")
        ProjectStatus status
)
 {
}
