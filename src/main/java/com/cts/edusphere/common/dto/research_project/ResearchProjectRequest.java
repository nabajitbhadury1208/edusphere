package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ResearchProjectRequest(

        @NotBlank(groups = OnCreate.class, message = "Project title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 200 characters")
        String title,

        @NotNull(groups = OnCreate.class,message = "Faculty is required")
        UUID facultyId,

        @NotNull(groups = OnCreate.class,message = "facultyMember cannot be null; send empty list if none")
        List<UUID> facultyMembers,

        @NotNull(groups = OnCreate.class,message = "student cannot be null; send empty list if none")
        List<UUID> students,

        @NotNull(groups = OnCreate.class,message = "startDate is required")
        LocalDate startDate,

        @NotNull(groups = OnCreate.class,message = "endDate is required")
        LocalDate endDate,

        @NotNull(groups = OnCreate.class,message = "status is required")
        ProjectStatus status
)
 {
}
