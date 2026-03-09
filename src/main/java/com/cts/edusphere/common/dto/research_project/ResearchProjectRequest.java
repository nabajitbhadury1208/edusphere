package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
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

        @NotBlank(groups = {OnCreate.class,OnUpdate.class})
        @Size(min = 3, max = 100, message = "Title must be between 3 and 200 characters")
        String title,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "Faculty is required")
        Faculty faculty,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "facultyMember cannot be null; send empty list if none")
        List<Faculty> facultyMembers,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "student cannot be null; send empty list if none")
        List<Student> students,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "startDate is required")
        LocalDate startDate,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "endDate is required")
        LocalDate endDate,

        @NotNull(groups = {OnCreate.class,OnUpdate.class},message = "status is required")
        ProjectStatus status
)
 {
}
