package com.cts.edusphere.services.research_project;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.FacultiesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectCreationFailureException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectDeletionFailed;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectUpdateFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentNotFoundException;
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
                    .orElseThrow(() -> new FacultyNotFoundException("Lead faculty not found"));

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
        }  catch (ResearchProjectCreationFailureException e) {
            log.error("Failed to create research project: {}", e.getMessage());
            throw new ResearchProjectCreationFailureException("Error occurred while creating project: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating research project: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while creating the project");
        }
    }

    @Override
    public ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId) {
        try {
            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));
                    
            Faculty coInvestigator = facultyRepository.findById(facultyId)
                    .orElseThrow(() -> new FacultyNotFoundException("faculty member not found"));
    
            project.getAssociatedFacultyMembers().add(coInvestigator);
            
            return projectMapper.toResponse(projectRepository.save(project));
        } catch (ResearchProjectUpdateFailedException e) {
            log.error("Failed to add faculty member to project {}: {}", projectId, e.getMessage());
            throw new ResearchProjectUpdateFailedException("Error occurred while adding faculty member: " + e.getMessage());
        } catch (FacultiesNotFoundException e) {
            log.error("Faculty member with ID {} not found: {}", facultyId, e.getMessage());
            throw new FacultyNotFoundException("Faculty member with ID: " + facultyId + " not found");
        } catch (Exception e) {
            log.error("Unexpected error occurred while adding faculty member to project {}: {}", projectId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the project");
        }
    }

    @Override
    public ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId) {
        try {
            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));
        
            project.getAssociatedFacultyMembers().removeIf(f -> f.getId().equals(facultyId));
            return projectMapper.toResponse(projectRepository.save(project));
        } catch (ResearchProjectUpdateFailedException e) {
            log.error("Failed to remove faculty member from project {}: {}", projectId, e.getMessage());
            throw new ResearchProjectUpdateFailedException("Error occurred while removing faculty member: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while removing faculty member from project {}: {}", projectId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the project");
        }
    }

    @Override
    public ResearchProjectResponse addStudent(UUID projectId, UUID studentId) {
        try {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("student not found"));

        project.getParticipatedStudents().add(student);
        return projectMapper.toResponse(projectRepository.save(project));
        } catch (ResearchProjectUpdateFailedException e) {
            log.error("Failed to add student to project {}: {}", projectId, e.getMessage());
            throw new ResearchProjectUpdateFailedException("Error occurred while adding student: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while adding student to project {}: {}", projectId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the project");
        }
    }

    @Override
    public ResearchProjectResponse removeStudent(UUID projectId, UUID studentId) {
        try {
            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));

            project.getParticipatedStudents().removeIf(s -> s.getId().equals(studentId));
            return projectMapper.toResponse(projectRepository.save(project));
        } catch (ResearchProjectUpdateFailedException e) {
            log.error("Failed to remove student from project {}: {}", projectId, e.getMessage());
            throw new ResearchProjectUpdateFailedException("Error occurred while removing student: " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error occurred while removing student from project {}: {}", projectId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the project");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchProjectResponse> getAllProjects() {
        try {
            return projectRepository.findAll().stream()
                    .map(projectMapper::toResponse)
                    .collect(Collectors.toList());

        } catch (ResearchProjectNotFoundException e) {
            log.error("Error occurred while fetching all research projects: {}", e.getMessage());

            throw new ResearchProjectNotFoundException("Failed to retrieve research projects list");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all research projects: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve research projects list");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchProjectResponse getProjectById(UUID id) {
        try {
        return projectRepository.findById(id)
                .map(projectMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        } catch (ResearchProjectNotFoundException e) {
            log.error("Research project with ID {} not found: {}", id, e.getMessage());
            throw new ResearchProjectNotFoundException("Project with ID: " + id + " not found");

        } catch (Exception e) {
            log.error("Error occurred while fetching research project with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve research project details");
        }
    }

    @Override
    public void deleteProject(UUID id) {
        try {
            if (!projectRepository.existsById(id)) {
                throw new ResourceNotFoundException("Project not found");
            }
    
            projectRepository.deleteById(id);
            log.info("Research project with ID {} deleted", id);

        } catch (ResearchProjectDeletionFailed e) {
            log.error("Failed to delete research project {}: {}", id, e.getMessage());
            throw new ResearchProjectDeletionFailed("Error occurred while deleting project: " + e.getMessage());
            
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting research project {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while deleting the project");
        }
    }
}