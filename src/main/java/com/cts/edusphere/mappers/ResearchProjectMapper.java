package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.ResearchProject;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.modules.User;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ResearchProjectMapper {

    public ResearchProject toEntity(ResearchProjectRequest request, Faculty leadFaculty, List<UUID> members, List<UUID> students) {
//
        return ResearchProject.builder()
                .title(request.title())
                .faculty(Faculty.builder().id(request.facultyId()).build())
                .facultyMembers(
                        request.facultyMembers().stream()
                                .map(id -> Faculty.builder().id(id).build())
                                .collect(Collectors.toList())
                )
                .students(
                        request.students().stream()
                                .map(id -> Student.builder().id(id).build())
                                .collect(Collectors.toList())
                )
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(request.status())
                .build();


    }

    public ResearchProjectResponse toResponse(ResearchProject project) {
        if (project == null) return null;
        return new ResearchProjectResponse(
                project.getTitle(),
                project.getFaculty(),
                project.getStatus(),
                project.getEndDate(),
                project.getStartDate(),
                project.getFacultyMembers(),
                project.getStudents()
        );
    }
}