package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.ResearchProject;
import com.cts.edusphere.modules.Student;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ResearchProjectMapper {

    public ResearchProject toEntity(ResearchProjectRequest request, Faculty leadFaculty, List<Faculty> members, List<Student> students) {
        if (request == null) return null;
        ResearchProject project = new ResearchProject();
        project.setTitle(request.title());
        project.setFaculty(leadFaculty);
        project.setFacultyMembers(members);
        project.setStudents(students);
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        return project;
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