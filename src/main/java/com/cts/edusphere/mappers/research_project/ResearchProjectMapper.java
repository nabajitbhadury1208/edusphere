package com.cts.edusphere.mappers.research_project;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.research_project.ResearchProject;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ResearchProjectMapper {

    public ResearchProject toEntity(ResearchProjectRequest request, Faculty leadFaculty, List<Faculty> members, List<Student> students) {
        return ResearchProject.builder()
                .title(request.title())
                .facultyLead(leadFaculty)
                .associatedFacultyMembers(members)
                .participatedStudents(students)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(request.status())
                .build();
    }

    public ResearchProjectResponse toResponse(ResearchProject project) {
        if (project == null) return null;

        List<UUID> facultyIds = project.getAssociatedFacultyMembers().stream()
                .map(Faculty::getId)
                .toList();

        List<UUID> studentIds = project.getParticipatedStudents().stream()
                .map(Student::getId)
                .toList();

        return new ResearchProjectResponse(
                project.getTitle(),
                project.getFacultyLead().getId(),
                project.getStatus(),
                project.getEndDate(),
                project.getStartDate(),
                facultyIds,
                studentIds
        );
    }
}