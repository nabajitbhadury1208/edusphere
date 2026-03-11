package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
