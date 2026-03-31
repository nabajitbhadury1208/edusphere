package com.cts.edusphere.services.research_project;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.FacultiesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectCreationFailureException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectDeletionFailed;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ResearchProjectUpdateFailedException;
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

/**
 * Service implementation for managing research projects within the EduSphere platform.
 *
 * <p>Provides full lifecycle operations for research projects, including creation,
 * retrieval, membership management (faculty and students), and deletion. All
 * write operations participate in the surrounding Spring-managed transaction declared
 * at the class level. Read-only operations override the transaction with
 * {@code readOnly = true} for performance optimisation.</p>
 *
 * <p>Compliance auditing is applied selectively at the method level via
 * {@link com.cts.edusphere.aspects.ComplianceAudit} to ensure that regulated
 * operations (e.g. project creation) are traceable for funding and ethical-clearance
 * purposes.</p>
 *
 * @see ResearchProjectService
 * @see com.cts.edusphere.modules.research_project.ResearchProject
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResearchProjectServiceImpl implements ResearchProjectService {

    private final ResearchProjectRepository projectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final ResearchProjectMapper projectMapper;

    /**
     * Creates a new research project and persists it to the database.
     *
     * <p>Resolves the lead faculty, all co-investigator faculty members, and the
     * participating students referenced in the request before delegating to the
     * mapper to build the entity. A compliance audit trail is recorded automatically
     * by the {@code @ComplianceAudit} aspect to verify funding sources and ethical
     * clearance.</p>
     *
     * @param request the {@link ResearchProjectRequest} DTO containing project title,
     *                lead faculty ID, co-investigator faculty IDs, and student IDs
     * @return a {@link ResearchProjectResponse} representing the newly persisted project
     * @throws FacultyNotFoundException               if the lead faculty ID does not match
     *                                               any existing faculty record
     * @throws ResearchProjectCreationFailureException if a domain-level creation constraint
     *                                               is violated
     * @throws InternalServerErrorException          if any other unexpected error occurs
     *                                               during persistence
     */
    @Override
    @ComplianceAudit(entityType = AuditEntityType.RESEARCH_APPROVAL, scope = "Verify funding sources and ethical clearance")
    public ResearchProjectResponse createProject(ResearchProjectRequest request) {

            Faculty lead = facultyRepository.findById(request.facultyId())
                    .orElseThrow(() -> new FacultyNotFoundException("Lead faculty not found"));
        try {
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
        }
        catch (ResearchProjectCreationFailureException e) {
            log.error("Failed to create research project: {}", e.getMessage());
            throw new ResearchProjectCreationFailureException("Error occurred while creating project: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating research project: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while creating the project");
        }
    }

    /**
     * Adds an existing faculty member as a co-investigator to an existing research project.
     *
     * <p>Both the project and the faculty record must already exist in the database.
     * The faculty member is appended to the project's associated faculty members
     * collection and the updated project is immediately saved.</p>
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param facultyId the {@link UUID} of the faculty member to add as a co-investigator
     * @return a {@link ResearchProjectResponse} reflecting the updated project state
     * @throws ResearchProjectNotFoundException      if no project exists for the given
     *                                              {@code projectId}
     * @throws FacultyNotFoundException              if no faculty record exists for the
     *                                              given {@code facultyId}
     * @throws ResearchProjectUpdateFailedException  if a domain-level update constraint
     *                                              is violated
     * @throws InternalServerErrorException         if any other unexpected error occurs
     */
    @Override
    public ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId) {

            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));
                    
            Faculty coInvestigator = facultyRepository.findById(facultyId)
                    .orElseThrow(() -> new FacultyNotFoundException("Faculty memberwith ID: " + facultyId + "not found"));
        try {
            project.getAssociatedFacultyMembers().add(coInvestigator);
            
            return projectMapper.toResponse(projectRepository.save(project));
        }
        catch (ResearchProjectUpdateFailedException e) {
            log.error("Failed to add faculty member to project {}: {}", projectId, e.getMessage());
            throw new ResearchProjectUpdateFailedException("Error occurred while adding faculty member: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while adding faculty member to project {}: {}", projectId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the project");
        }
    }

    /**
     * Removes a faculty member from the co-investigator list of a research project.
     *
     * <p>The project must already exist. The faculty member whose {@link UUID} matches
     * {@code facultyId} is removed from the associated faculty members collection using
     * an identity comparison, and the updated project is saved. If no matching faculty
     * member is present in the collection, the save still succeeds with no change.</p>
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param facultyId the {@link UUID} of the faculty member to remove
     * @return a {@link ResearchProjectResponse} reflecting the updated project state
     * @throws ResearchProjectNotFoundException      if no project exists for the given
     *                                              {@code projectId}
     * @throws ResearchProjectUpdateFailedException  if a domain-level update constraint
     *                                              is violated
     * @throws InternalServerErrorException         if any other unexpected error occurs
     */
    @Override
    public ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId) {

            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));
        try {
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

    /**
     * Enrolls a student as a participant in an existing research project.
     *
     * <p>Both the project and the student record must already exist in the database.
     * The student is appended to the project's participated-students collection and
     * the updated project is immediately saved.</p>
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param studentId the {@link UUID} of the student to enrol as a participant
     * @return a {@link ResearchProjectResponse} reflecting the updated project state
     * @throws ResearchProjectNotFoundException     if no project exists for the given
     *                                             {@code projectId}
     * @throws StudentNotFoundException             if no student record exists for the
     *                                             given {@code studentId}
     * @throws ResearchProjectUpdateFailedException if a domain-level update constraint
     *                                             is violated
     * @throws InternalServerErrorException        if any other unexpected error occurs
     */
    @Override
    public ResearchProjectResponse addStudent(UUID projectId, UUID studentId) {

        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("student not found"));
        try {
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

    /**
     * Removes a student from the participant list of a research project.
     *
     * <p>The project must already exist. The student whose {@link UUID} matches
     * {@code studentId} is removed from the participated-students collection using
     * an identity comparison, and the updated project is saved. If no matching student
     * is present in the collection, the save still succeeds with no change.</p>
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param studentId the {@link UUID} of the student to remove
     * @return a {@link ResearchProjectResponse} reflecting the updated project state
     * @throws ResearchProjectNotFoundException     if no project exists for the given
     *                                             {@code projectId}
     * @throws ResearchProjectUpdateFailedException if a domain-level update constraint
     *                                             is violated
     * @throws InternalServerErrorException        if any other unexpected error occurs
     */
    @Override
    public ResearchProjectResponse removeStudent(UUID projectId, UUID studentId) {

            ResearchProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found"));
        try {
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

    /**
     * Retrieves all research projects stored in the database.
     *
     * <p>This is a read-only transactional operation. Every persisted
     * {@link com.cts.edusphere.modules.research_project.ResearchProject} is mapped to a
     * {@link ResearchProjectResponse} DTO and returned as a list. The list will be
     * empty if no projects have been created yet.</p>
     *
     * @return a {@link List} of {@link ResearchProjectResponse} DTOs representing all
     *         research projects; never {@code null}
     * @throws ResearchProjectNotFoundException if a domain-level retrieval constraint
     *                                         is violated
     * @throws InternalServerErrorException    if any other unexpected error occurs
     *                                         during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResearchProjectResponse> getAllProjects() {
        List<ResearchProject> projects = projectRepository.findAll();


        if (projects.isEmpty()) {
            throw new ResearchProjectNotFoundException("No research projects found in the system.");
        }
        try {
            return projects.stream()
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

    /**
     * Retrieves a single research project by its unique identifier.
     *
     * <p>This is a read-only transactional operation. The project entity is fetched
     * from the repository by its {@link UUID} primary key and mapped to a response DTO.
     * An exception is thrown when no matching record is found.</p>
     *
     * @param id the {@link UUID} primary key of the research project to retrieve
     * @return a {@link ResearchProjectResponse} representing the found project
     * @throws ResourceNotFoundException        if no project exists for the given {@code id}
     * @throws ResearchProjectNotFoundException if a domain-level not-found constraint
     *                                         is violated
     * @throws InternalServerErrorException    if any other unexpected error occurs
     *                                         during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public ResearchProjectResponse getProjectById(UUID id) {
        try {
        return projectRepository.findById(id)
                .map(projectMapper::toResponse)
                .orElseThrow(() -> new ResearchProjectNotFoundException("Project not found with ID: " + id));

        } catch (ResearchProjectNotFoundException e) {
            log.error("Research project with ID {} not found: {}", id, e.getMessage());
            throw new ResearchProjectNotFoundException("Project with ID: " + id + " not found");
        } catch (Exception e) {
            log.error("Error occurred while fetching research project with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve research project details");
        }
    }

    /**
     * Permanently deletes a research project identified by its unique identifier.
     *
     * <p>Existence of the project is verified before deletion is attempted. If the
     * project is not found, a {@link ResourceNotFoundException} is thrown immediately
     * without attempting any database write. On successful deletion a log entry at
     * INFO level is emitted.</p>
     *
     * @param id the {@link UUID} primary key of the research project to delete
     * @throws ResourceNotFoundException      if no project exists for the given {@code id}
     * @throws ResearchProjectDeletionFailed  if a domain-level deletion constraint is
     *                                       violated
     * @throws InternalServerErrorException  if any other unexpected error occurs during
     *                                       deletion
     */
    @Override
    public void deleteProject(UUID id) {

            if (!projectRepository.existsById(id)) {
                throw new ResourceNotFoundException("Project not found");
            }
        try {
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