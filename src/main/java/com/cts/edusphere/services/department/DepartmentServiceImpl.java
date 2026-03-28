package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentCouldNotBeDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentCouldNotBeUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InsufficientPermissionException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.mappers.department.DepartmentMapper;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.department.DepartmentRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing department-related operations.
 *
 * <p>Provides transactional CRUD operations for {@link Department} entities, including
 * creation, retrieval, update, department-head reassignment, and deletion. All write
 * operations run within a transaction; read-only operations use a read-only transaction
 * for performance optimisation.
 *
 * <p>Authorisation is enforced at the business layer: only users that hold the
 * {@link Role#DEPARTMENT_HEAD} or {@link Role#ADMIN} role may be assigned as the head
 * of a department.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;
  private final DepartmentMapper departmentMapper;

  /**
   * Creates a new department from the supplied request data.
   *
   * <p>If a {@code headId} is present in the request, the corresponding user is looked up
   * and validated to ensure they hold the {@link Role#DEPARTMENT_HEAD} or {@link Role#ADMIN}
   * role before being set as the department head.
   *
   * @param requestDTO the DTO containing the new department's details, including an optional
   *                   {@code headId} for the department head
   * @return a {@link DepartmentResponseDTO} representing the persisted department
   * @throws UserNotFoundException           if a {@code headId} is provided but no matching
   *                                         user can be found
   * @throws InsufficientPermissionException if the resolved user does not hold the required role
   * @throws DepartmentNotCreatedException   if a domain-level error prevents department creation
   * @throws InternalServerErrorException    if an unexpected error occurs during creation
   */
  @Override
  public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
    try {
      Department department = departmentMapper.toEntity(requestDTO);

      if (requestDTO.headId() != null) {
        User departmentHead =
            userRepository
                .findById(requestDTO.headId())
                .orElseThrow(
                    () ->
                        new UserNotFoundException(
                            "department Head not found with id: " + requestDTO.headId()));

        if (!departmentHead.getRoles().contains(Role.DEPARTMENT_HEAD)
            && !departmentHead.getRoles().contains(Role.ADMIN)) {
          throw new InsufficientPermissionException(
              "User with id: " + requestDTO.headId() + " is not a department head");
        }

        department.setDepartmentHead(departmentHead);
      }

      Department savedDepartment = departmentRepository.save(department);
      log.info("department created successfully: {}", savedDepartment.getDepartmentName());

      return departmentMapper.toResponseDTO(savedDepartment);
    } catch (DepartmentNotCreatedException e) {
      log.error("Error creating department: {}", e.getMessage());
      throw new DepartmentNotCreatedException("Failed to create department");
    } catch (Exception e) {
      log.error("Unexpected error occurred while creating department: {}", e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while creating the department");
    }
  }

  /**
   * Retrieves a single department by its unique identifier.
   *
   * @param id the {@link UUID} of the department to retrieve
   * @return a {@link DepartmentResponseDTO} representing the found department
   * @throws DepartmentNotFoundException  if no department exists with the given {@code id}
   * @throws InternalServerErrorException if an unexpected error occurs during retrieval
   */
  @Override
  @Transactional(readOnly = true)
  public DepartmentResponseDTO getDepartmentById(UUID id) {
    try {
      Department department =
          departmentRepository
              .findById(id)
              .orElseThrow(
                  () -> new DepartmentNotFoundException("department not found with id: " + id));

      return departmentMapper.toResponseDTO(department);
    } catch (DepartmentNotFoundException e) {
      log.error("Error retrieving department with id {}: {}", id, e.getMessage());
      throw new DepartmentNotFoundException("Failed to retrieve department with id: " + id);
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while retrieving department with id {}: {}",
          id,
          e.getMessage());
      throw new InternalServerErrorException("Failed to retrieve department with id: " + id);
    }
  }

  /**
   * Retrieves all departments persisted in the system.
   *
   * @return a {@link List} of {@link DepartmentResponseDTO} objects representing every
   *         department; never {@code null}, but may be empty
   * @throws DepartmentsNotFoundException if a domain-level error signals that departments
   *                                      cannot be retrieved
   * @throws InternalServerErrorException if an unexpected error occurs during retrieval
   */
  @Override
  @Transactional(readOnly = true)
  public List<DepartmentResponseDTO> getAllDepartments() {
    try {
      return departmentRepository.findAll().stream()
          .map(departmentMapper::toResponseDTO)
          .collect(Collectors.toList());
    } catch (DepartmentsNotFoundException e) {
      log.error("Error retrieving all departments: {}", e.getMessage());
      throw new DepartmentsNotFoundException("Failed to retrieve departments list");
    } catch (Exception e) {
      log.error("Unexpected error occurred while retrieving all departments: {}", e.getMessage());
      throw new InternalServerErrorException("Failed to retrieve departments list");
    }
  }

  /**
   * Partially updates an existing department with the values supplied in the request DTO.
   *
   * <p>Only non-{@code null} fields in {@code requestDTO} are applied to the stored entity,
   * allowing callers to perform partial (PATCH-style) updates. If a new {@code headId} is
   * provided, the referenced user is validated for the required role before being assigned.
   *
   * @param id         the {@link UUID} of the department to update
   * @param requestDTO the DTO containing the fields to update; {@code null} fields are ignored
   * @return a {@link DepartmentResponseDTO} representing the updated department
   * @throws DepartmentNotFoundException        if no department exists with the given {@code id}
   * @throws UserNotFoundException              if a {@code headId} is supplied but the user
   *                                            cannot be found
   * @throws InsufficientPermissionException    if the resolved user does not hold the required role
   * @throws DepartmentCouldNotBeUpdatedException if a domain-level error prevents the update
   * @throws InternalServerErrorException       if an unexpected error occurs during the update
   */
  @Override
  public DepartmentResponseDTO updateDepartment(UUID id, DepartmentRequestDTO requestDTO) {
    try {
      Department department =
          departmentRepository
              .findById(id)
              .orElseThrow(
                  () -> new DepartmentNotFoundException("department not found with id: " + id));

      if (requestDTO.departmentName() != null)
        department.setDepartmentName(requestDTO.departmentName());
      if (requestDTO.departmentCode() != null)
        department.setDepartmentCode(requestDTO.departmentCode());
      if (requestDTO.contactInfo() != null) department.setContactInfo(requestDTO.contactInfo());
      if (requestDTO.status() != null) department.setStatus(requestDTO.status());

      if (requestDTO.headId() != null) {

        User departmentHead =
            userRepository
                .findById(requestDTO.headId())
                .orElseThrow(
                    () ->
                        new UserNotFoundException(
                            "department Head not found with id: " + requestDTO.headId()));
        if (!departmentHead.getRoles().contains(Role.DEPARTMENT_HEAD)
            && !departmentHead.getRoles().contains(Role.ADMIN)) {
          throw new InsufficientPermissionException(
              "User with id: " + requestDTO.headId() + " is not a department head");
        }
        department.setDepartmentHead(departmentHead);
      }

      Department updatedDepartment = departmentRepository.save(department);
      log.info("department partially updated: {}", id);

      return departmentMapper.toResponseDTO(updatedDepartment);

    } catch (DepartmentCouldNotBeUpdatedException e) {
      log.error("Error partially updating department {}: {}", id, e.getMessage());
      throw new DepartmentCouldNotBeUpdatedException("Failed to partially update department");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while partially updating department {}: {}",
          id,
          e.getMessage());
      throw new InternalServerErrorException("Failed to partially update department");
    }
  }

  /**
   * Reassigns the head of an existing department to a different user.
   *
   * <p>Both the department and the prospective new head are looked up by their respective
   * identifiers. No role validation is performed here beyond verifying the user exists;
   * role enforcement is expected to have occurred upstream.
   *
   * @param id     the {@link UUID} of the department whose head is to be changed
   * @param headId the {@link UUID} of the user to assign as the new department head
   * @return a {@link DepartmentResponseDTO} representing the updated department
   * @throws DepartmentNotFoundException          if no department exists with the given {@code id}
   * @throws UserNotFoundException                if no user exists with the given {@code headId}
   * @throws DepartmentCouldNotBeUpdatedException if a domain-level error prevents the update
   * @throws InternalServerErrorException         if an unexpected error occurs during the update
   */
  @Override
  public DepartmentResponseDTO changeDepartmentHead(UUID id, UUID headId) {
    try {
      Department department =
          departmentRepository
              .findById(id)
              .orElseThrow(
                  () -> new DepartmentNotFoundException("department not found with id: " + id));

      User departmentHead =
          userRepository
              .findById(headId)
              .orElseThrow(
                  () -> new UserNotFoundException("department Head not found with id: " + headId));

      department.setDepartmentHead(departmentHead);

      Department updatedDepartment = departmentRepository.save(department);
      log.info("department head changed for department: {}", id);

      return departmentMapper.toResponseDTO(updatedDepartment);
    } catch (DepartmentCouldNotBeUpdatedException e) {
      log.error("Error changing department head for {}: {}", id, e.getMessage());
      throw new DepartmentCouldNotBeUpdatedException("Failed to change department head");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while changing department head for {}: {}",
          id,
          e.getMessage());
      throw new InternalServerErrorException("Failed to change department head");
    }
  }

  /**
   * Deletes the department identified by the given {@code id}.
   *
   * <p>The department is first fetched to confirm it exists before the delete is issued,
   * ensuring a meaningful error is returned rather than a silent no-op.
   *
   * @param id the {@link UUID} of the department to delete
   * @throws DepartmentNotFoundException          if no department exists with the given {@code id}
   * @throws DepartmentCouldNotBeDeletedException if a domain-level error prevents deletion
   * @throws InternalServerErrorException         if an unexpected error occurs during deletion
   */
  @Override
  public void deleteDepartment(UUID id) {
    try {
      Department department =
          departmentRepository
              .findById(id)
              .orElseThrow(
                  () -> new DepartmentNotFoundException("department not found with id: " + id));
      departmentRepository.delete(department);
      log.info("department deleted: {}", id);

    } catch (DepartmentCouldNotBeDeletedException e) {
      log.error("Error deleting department {}: {}", id, e.getMessage());
      throw new DepartmentCouldNotBeDeletedException("Failed to delete department");
    } catch (Exception e) {
      log.error("Unexpected error occurred while deleting department {}: {}", id, e.getMessage());
      throw new InternalServerErrorException("Failed to delete department");
    }
  }
}
