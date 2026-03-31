package com.cts.edusphere.services.research_project;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for research project management within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, and deleting research projects, as well as
 * managing the membership of faculty co-investigators and student contributors. All operations
 * use Data Transfer Objects (DTOs) to decouple the service layer from the underlying
 * persistence model.</p>
 */
public interface ResearchProjectService {

    // Sl No 1 & 2 & 3

    /**
     * Creates a new research project from the provided request data.
     *
     * @param request the {@link ResearchProjectRequest} containing the project details
     *                (title, description, principal investigator, etc.); must not be {@code null}
     * @return a {@link ResearchProjectResponse} representing the newly created research project
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if a referenced entity
     *         (e.g., principal investigator faculty member) does not exist
     */
    ResearchProjectResponse createProject(ResearchProjectRequest request);

    /**
     * Retrieves all research projects in the system.
     *
     * @return a {@link List} of {@link ResearchProjectResponse} objects representing all projects;
     *         never {@code null}, may be empty
     */
    List<ResearchProjectResponse> getAllProjects();

    /**
     * Retrieves a single research project by its unique identifier.
     *
     * @param id the {@link UUID} of the research project to retrieve
     * @return a {@link ResearchProjectResponse} representing the found project
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given ID
     */
    ResearchProjectResponse getProjectById(UUID id);

    // Sl No 6 & 7 (Co-investigators)

    /**
     * Adds a faculty member as a co-investigator to an existing research project.
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param facultyId the {@link UUID} of the faculty member to add as a co-investigator
     * @return a {@link ResearchProjectResponse} reflecting the updated co-investigator list
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given projectId,
     *         or if no faculty member exists with the given facultyId
     */
    ResearchProjectResponse addFacultyMember(UUID projectId, UUID facultyId);

    /**
     * Removes a faculty member from the co-investigator list of a research project.
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param facultyId the {@link UUID} of the faculty member to remove
     * @return a {@link ResearchProjectResponse} reflecting the updated co-investigator list
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given projectId,
     *         or if no faculty member exists with the given facultyId
     */
    ResearchProjectResponse removeFacultyMember(UUID projectId, UUID facultyId);

    // Sl No 8 & 9 (Students)

    /**
     * Adds a student as a contributor to an existing research project.
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param studentId the {@link UUID} of the student to add to the project
     * @return a {@link ResearchProjectResponse} reflecting the updated student contributor list
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given projectId,
     *         or if no student exists with the given studentId
     */
    ResearchProjectResponse addStudent(UUID projectId, UUID studentId);

    /**
     * Removes a student from the contributor list of a research project.
     *
     * @param projectId the {@link UUID} of the research project to update
     * @param studentId the {@link UUID} of the student to remove
     * @return a {@link ResearchProjectResponse} reflecting the updated student contributor list
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given projectId,
     *         or if no student exists with the given studentId
     */
    ResearchProjectResponse removeStudent(UUID projectId, UUID studentId);

    // Sl No 10

    /**
     * Deletes the research project identified by the given ID.
     *
     * @param id the {@link UUID} of the research project to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no project exists with the given ID
     */
    void deleteProject(UUID id);

}
