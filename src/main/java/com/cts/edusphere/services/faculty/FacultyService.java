package com.cts.edusphere.services.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for faculty management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting faculty records,
 * as well as querying faculty members by their associated department. All operations
 * use Data Transfer Objects (DTOs) to decouple the service layer from the persistence model.</p>
 */
public interface FacultyService {

    /**
     * Creates a new faculty record from the provided request data.
     *
     * @param requestDTO the {@link FacultyRequestDTO} containing the faculty member's details; must not be {@code null}
     * @return a {@link FacultyResponseDTO} representing the newly created faculty record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException  if the referenced department does not exist
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a faculty member with the same identifying information already exists
     */
    FacultyResponseDTO createFaculty(FacultyRequestDTO requestDTO);

    /**
     * Retrieves a single faculty record by its unique identifier.
     *
     * @param id the {@link UUID} of the faculty member to retrieve
     * @return a {@link FacultyResponseDTO} representing the found faculty record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no faculty member exists with the given ID
     */
    FacultyResponseDTO getFacultyById(UUID id);

    /**
     * Retrieves all faculty records in the system.
     *
     * @return a {@link List} of {@link FacultyResponseDTO} objects representing all faculty members;
     *         never {@code null}, may be empty
     */
    List<FacultyResponseDTO> getAllFaculties();

    /**
     * Retrieves all faculty members belonging to a specific department.
     *
     * @param departmentId the {@link UUID} of the department whose faculty members are to be retrieved
     * @return a {@link List} of {@link FacultyResponseDTO} objects for the given department;
     *         never {@code null}, may be empty if the department has no faculty
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID
     */
    List<FacultyResponseDTO> getFacultiesByDepartment(UUID departmentId);

    /**
     * Updates the details of an existing faculty record.
     *
     * @param id         the {@link UUID} of the faculty member to update
     * @param requestDTO the {@link FacultyRequestDTO} containing the updated field values; must not be {@code null}
     * @return a {@link FacultyResponseDTO} representing the updated faculty record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no faculty member exists with the given ID
     */
    FacultyResponseDTO updateFaculty(UUID id, FacultyRequestDTO requestDTO);

    /**
     * Deletes the faculty record identified by the given ID.
     *
     * @param id the {@link UUID} of the faculty member to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no faculty member exists with the given ID
     */
    void deleteFaculty(UUID id);
}
