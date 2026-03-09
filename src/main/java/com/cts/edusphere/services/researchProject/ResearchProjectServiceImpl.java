package com.cts.edusphere.services.researchProject;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ResearchProjectMapper;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.ResearchProject;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.repositories.FacultyRepository;
import com.cts.edusphere.repositories.ResearchProjectRepository;
import com.cts.edusphere.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResearchProjectServiceImpl implements ResearchProjectService {

    private final ResearchProjectRepository projectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final ResearchProjectMapper projectMapper;

    @Override
    public ResearchProjectResponse createProject(ResearchProjectRequest request) {
        try {
            Faculty lead = facultyRepository.findById(request.faculty().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead Faculty not found"));

            ResearchProject project = projectMapper.toEntity(request, lead, request.facultyMembers(), request.students());
            ResearchProject saved = projectRepository.save(project);
            log.info("Research project '{}' created with Lead Faculty ID: {}", saved.getTitle(), lead.getId());
            return projectMapper.toResponse(saved);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create research project: {}", e.getMessage());
            throw new InternalServerErrorException("Error occurred while creating project");
        }
    }

    @Override
    public ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Faculty coInvestigator = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found"));

        project.getFacultyMembers().add(coInvestigator);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.getFacultyMembers().removeIf(f -> f.getId().equals(facultyId));
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse addStudent(UUID projectId, UUID studentId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        project.getStudents().add(student);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse removeStudent(UUID projectId, UUID studentId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.getStudents().removeIf(s -> s.getId().equals(studentId));
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchProjectResponse getProjectById(UUID id) {
        return projectRepository.findById(id)
                .map(projectMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
    }

    @Override
    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
        log.info("Research project with ID {} deleted", id);
    }
}