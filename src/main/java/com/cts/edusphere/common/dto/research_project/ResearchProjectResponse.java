package com.cts.edusphere.common.dto.research_project;

import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;

import java.time.LocalDate;
import java.util.List;

public record ResearchProjectResponse(
        String title,
        Faculty faculty,
        ProjectStatus status,
        LocalDate endDate,
        LocalDate startDate,
        List<Faculty> facultyMembers,
        List<Student> students
) {
}
