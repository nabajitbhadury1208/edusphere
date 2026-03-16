package com.cts.edusphere.services.research_project;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.research_project.ResearchProjectMapper;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.research_project.ResearchProject;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.faculty.FacultyRepository;
import com.cts.edusphere.repositories.research_project.ResearchProjectRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
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
    @ComplianceAudit(entityType = AuditEntityType.RESEARCH_APPROVAL, scope = "Verify funding sources and ethical clearance")
    public ResearchProjectResponse createProject(ResearchProjectRequest request) {
        try {
            Faculty lead = facultyRepository.findById(request.facultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead faculty not found"));
            List<Faculty> members = request.facultyMembers().stream()
                    .map(facultyRepository::getReferenceById)
                    .toList();

            List<Student> students = request.students().stream()
                    .map(studentRepository::getReferenceById)
                    .toList();
            ResearchProject project = projectMapper.toEntity(request, lead, members, students);

            ResearchProject saved = projectRepository.save(project);
            log.info("Research project '{}' created with Lead faculty ID: {}", saved.getTitle(), lead.getId());
            return projectMapper.toResponse(saved);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create research project: {}", e.getMessage());
            throw new InternalServerErrorException("Error occurred while creating project: " + e.getMessage());
        }
    }

    @Override
    public ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Faculty coInvestigator = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("faculty member not found"));

        project.getAssociatedFacultyMembers().add(coInvestigator);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.getAssociatedFacultyMembers().removeIf(f -> f.getId().equals(facultyId));
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse addStudent(UUID projectId, UUID studentId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("student not found"));

        project.getParticipatedStudents().add(student);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ResearchProjectResponse removeStudent(UUID projectId, UUID studentId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.getParticipatedStudents().removeIf(s -> s.getId().equals(studentId));
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