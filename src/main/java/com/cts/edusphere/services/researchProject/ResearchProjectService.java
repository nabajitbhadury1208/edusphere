package com.cts.edusphere.services.researchProject;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import java.util.List;
import java.util.UUID;

public interface ResearchProjectService {
    // Sl No 1 & 2 & 3
    ResearchProjectResponse createProject(ResearchProjectRequest request);
    List<ResearchProjectResponse> getAllProjects();
    ResearchProjectResponse getProjectById(UUID id);

    // Sl No 6 & 7 (Co-investigators)
    ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId);
    ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId);

    // Sl No 8 & 9 (Students)
    ResearchProjectResponse addStudent(UUID projectId, UUID studentId);
    ResearchProjectResponse removeStudent(UUID projectId, UUID studentId);

    // Sl No 10
    void deleteProject(UUID id);
}