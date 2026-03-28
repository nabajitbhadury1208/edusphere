package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for department management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, deleting, and reassigning the
 * head of academic departments. All operations use Data Transfer Objects (DTOs) to
 * decouple the service layer from the underlying persistence model.</p>
 */
public interface DepartmentService {

    /**
     * Creates a new department from the provided request data.
     *
     * @param requestDTO the {@link DepartmentRequestDTO} containing the department details; must not be {@code null}
     * @return a {@link DepartmentResponseDTO} representing the newly created department
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a department with the same name or code already exists
     */
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);

    /**
     * Retrieves a single department by its unique identifier.
     *
     * @param id the {@link UUID} of the department to retrieve
     * @return a {@link DepartmentResponseDTO} representing the found department
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID
     */
    DepartmentResponseDTO getDepartmentById(UUID id);

    /**
     * Retrieves all departments in the system.
     *
     * @return a {@link List} of {@link DepartmentResponseDTO} objects representing all departments;
     *         never {@code null}, may be empty
     */
    List<DepartmentResponseDTO> getAllDepartments();

    /**
     * Updates the details of an existing department.
     *
     * @param id         the {@link UUID} of the department to update
     * @param requestDTO the {@link DepartmentRequestDTO} containing the updated field values; must not be {@code null}
     * @return a {@link DepartmentResponseDTO} representing the updated department
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID
     */
    DepartmentResponseDTO updateDepartment(UUID id, DepartmentRequestDTO requestDTO);

    /**
     * Assigns a new head to the specified department.
     *
     * @param id     the {@link UUID} of the department to update
     * @param headId the {@link UUID} of the faculty member to appoint as the new department head
     * @return a {@link DepartmentResponseDTO} reflecting the updated department head assignment
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID,
     *                                                                 or if no faculty member exists with the given headId
     */
    DepartmentResponseDTO changeDepartmentHead(UUID id, UUID headId);

    /**
     * Deletes the department identified by the given ID.
     *
     * @param id the {@link UUID} of the department to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID
     */
    void deleteDepartment(UUID id);
}
