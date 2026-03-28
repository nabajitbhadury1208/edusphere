package com.cts.edusphere.services.faculty;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FacultiesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyNotUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.FacultyServiceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.faculty.FacultyMapper;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.repositories.department.DepartmentRepository;
import com.cts.edusphere.repositories.faculty.FacultyRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing faculty-related business operations.
 *
 * <p>Provides CRUD operations for {@link Faculty} entities, including creation,
 * retrieval by ID or department, partial updates, and deletion. All write
 * operations are executed within a transaction by default; read-only operations
 * are annotated with {@code @Transactional(readOnly = true)} for performance.
 * Sensitive compliance actions are audited via the {@code @ComplianceAudit}
 * aspect.</p>
 *
 * <p>Dependencies are injected via constructor (Lombok {@code @RequiredArgsConstructor})
 * and logging is provided by Lombok {@code @Slf4j}.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FacultyServiceImpl implements FacultyService {

  private final FacultyRepository facultyRepository;
  private final DepartmentRepository departmentRepository;
  private final FacultyMapper facultyMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * Creates a new faculty member and persists them to the database.
   *
   * <p>Looks up the target department from the request, maps the request DTO to a
   * {@link Faculty} entity, assigns the {@link Role#FACULTY} role, encodes the
   * password, and saves the record. A compliance audit event of type
   * {@link AuditEntityType#FACULTY_CREATED} is recorded by the AOP aspect.</p>
   *
   * @param requestDTO the data transfer object containing the new faculty's details
   *                   (name, email, phone, password, position, status, departmentId)
   * @return a {@link FacultyResponseDTO} representing the persisted faculty record
   * @throws DepartmentNotFoundException if no department exists with the given
   *                                     {@code departmentId}
   * @throws FacultyNotCreatedException  if a domain-level creation constraint is violated
   * @throws InternalServerErrorException if any unexpected error occurs during creation
   */
  @Override
  @ComplianceAudit(
      entityType = AuditEntityType.FACULTY_CREATED,
      scope = "Verify new faculty background check and credentials")
  public FacultyResponseDTO createFaculty(FacultyRequestDTO requestDTO) {
    try {
      Department department =
          departmentRepository
              .findById(requestDTO.departmentId())
              .orElseThrow(
                  () ->
                      new DepartmentNotFoundException(
                          "department not found with id: " + requestDTO.departmentId()));

      Faculty faculty = facultyMapper.toEntity(requestDTO);

      faculty.setRoles(Set.of(Role.FACULTY));
      faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
      faculty.setDepartment(department);

      Faculty savedFaculty = facultyRepository.save(faculty);
      log.info("faculty created successfully with ID: {}", savedFaculty.getId());

      return facultyMapper.toResponseDTO(savedFaculty);
    } catch (FacultyNotCreatedException e) {
      log.error("Error creating faculty: {}", e.getMessage());
      throw new FacultyNotCreatedException("Failed to create faculty record");
    } catch (Exception e) {
      log.error("Unexpected error occurred while creating faculty: {}", e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while creating the faculty record");
    }
  }

  /**
   * Retrieves a single faculty member by their unique identifier.
   *
   * <p>Executes as a read-only transaction. The faculty entity is mapped to a
   * response DTO before being returned.</p>
   *
   * @param id the {@link UUID} of the faculty member to retrieve
   * @return a {@link FacultyResponseDTO} containing the faculty member's details
   * @throws FacultyNotFoundException      if no faculty record exists with the given {@code id}
   * @throws InternalServerErrorException  if any unexpected error occurs during retrieval
   */
  @Override
  @Transactional(readOnly = true)
  public FacultyResponseDTO getFacultyById(UUID id) {
    try {
      Faculty faculty =
          facultyRepository
              .findById(id)
              .orElseThrow(() -> new FacultyNotFoundException("faculty not found with id: " + id));

      return facultyMapper.toResponseDTO(faculty);
    } catch (FacultyNotFoundException e) {
      log.error("Error retrieving faculty {}: {}", id, e.getMessage());
      throw new FacultyNotFoundException("Failed to retrieve faculty details");
    } catch (Exception e) {
      log.error("Unexpected error occurred while retrieving faculty {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while retrieving faculty details");
    }
  }

  /**
   * Retrieves all faculty members stored in the system.
   *
   * <p>Executes as a read-only transaction. Each {@link Faculty} entity is mapped
   * to a {@link FacultyResponseDTO} and the full list is returned.</p>
   *
   * @return a {@link List} of {@link FacultyResponseDTO} objects; may be empty if
   *         no faculty records exist
   * @throws FacultiesNotFoundException   if a domain-level error prevents listing faculties
   * @throws InternalServerErrorException if any unexpected error occurs during retrieval
   */
  @Override
  @Transactional(readOnly = true)
  public List<FacultyResponseDTO> getAllFaculties() {
    try {
      return facultyRepository.findAll().stream()
          .map(facultyMapper::toResponseDTO)
          .collect(Collectors.toList());

    } catch (FacultiesNotFoundException e) {
      log.error("Error retrieving all faculties: {}", e.getMessage());
      throw new FacultiesNotFoundException("No faculties found");
    } catch (Exception e) {
      log.error("Error retrieving all faculties: {}", e.getMessage());
      throw new FacultiesNotFoundException("Failed to retrieve faculties list");
    }
  }

  /**
   * Retrieves all faculty members belonging to a specific department.
   *
   * <p>Executes as a read-only transaction. Looks up the {@link Department} by
   * {@code departmentId}, then streams its associated faculties through the mapper.</p>
   *
   * @param departmentId the {@link UUID} of the department whose faculty members are requested
   * @return a {@link List} of {@link FacultyResponseDTO} objects for the given department;
   *         may be empty if the department has no faculty assigned
   * @throws DepartmentNotFoundException  if no department exists with the given
   *                                      {@code departmentId}
   * @throws FacultiesNotFoundException   if a domain-level error prevents listing faculties
   * @throws InternalServerErrorException if any unexpected error occurs during retrieval
   */
  @Override
  @Transactional(readOnly = true)
  public List<FacultyResponseDTO> getFacultiesByDepartment(UUID departmentId) {
    try {
      Department department =
          departmentRepository
              .findById(departmentId)
              .orElseThrow(
                  () ->
                      new DepartmentNotFoundException(
                          "department not found with id: " + departmentId));

      return department.getFaculties().stream()
          .map(facultyMapper::toResponseDTO)
          .collect(Collectors.toList());

    } catch (FacultiesNotFoundException e) {
      log.error("Error retrieving faculties for department {}: {}", departmentId, e.getMessage());
      throw new FacultiesNotFoundException("Failed to retrieve department faculties");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while retrieving faculties for department {}: {}",
          departmentId,
          e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while retrieving department faculties");
    }
  }

  /**
   * Partially updates an existing faculty member's details.
   *
   * <p>Only non-null fields in {@code requestDTO} are applied to the persisted
   * entity, allowing callers to supply a sparse update payload. If a new
   * {@code departmentId} is provided, the faculty's department association is
   * also updated. The password, when supplied, is re-encoded before saving.</p>
   *
   * @param id         the {@link UUID} of the faculty member to update
   * @param requestDTO the data transfer object containing fields to update; fields
   *                   that are {@code null} are ignored
   * @return a {@link FacultyResponseDTO} reflecting the faculty member's state after
   *         the update
   * @throws FacultyNotFoundException      if no faculty record exists with the given {@code id}
   * @throws DepartmentNotFoundException   if a new {@code departmentId} is supplied but the
   *                                       department cannot be found
   * @throws FacultyNotUpdatedException    if a domain-level update constraint is violated
   * @throws InternalServerErrorException  if any unexpected error occurs during the update
   */
  @Override
  public FacultyResponseDTO updateFaculty(UUID id, FacultyRequestDTO requestDTO) {
    try {
      Faculty faculty =
          facultyRepository
              .findById(id)
              .orElseThrow(() -> new FacultyNotFoundException("faculty not found with id: " + id));

      if (requestDTO.name() != null) faculty.setName(requestDTO.name());
      if (requestDTO.email() != null) faculty.setEmail(requestDTO.email());
      if (requestDTO.phone() != null) faculty.setPhone(requestDTO.phone());
      if (requestDTO.password() != null)
        faculty.setPassword(passwordEncoder.encode(requestDTO.password()));
      if (requestDTO.position() != null) faculty.setPosition(requestDTO.position());
      if (requestDTO.status() != null) faculty.setStatus(requestDTO.status());

      if (requestDTO.departmentId() != null) {
        Department department =
            departmentRepository
                .findById(requestDTO.departmentId())
                .orElseThrow(
                    () ->
                        new DepartmentNotFoundException(
                            "department not found with id: " + requestDTO.departmentId()));

        faculty.setDepartment(department);
      }

      Faculty updatedFaculty = facultyRepository.save(faculty);

      log.info("faculty partially updated: {}", id);
      return facultyMapper.toResponseDTO(updatedFaculty);

    } catch (FacultyNotUpdatedException e) {
      log.error("Error partially updating faculty {}: {}", id, e.getMessage());
      throw new FacultyNotUpdatedException("Failed to partially update faculty");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while partially updating faculty {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while partially updating the faculty record");
    }
  }

  /**
   * Deletes a faculty member from the system by their unique identifier.
   *
   * <p>Locates the faculty record and removes it from the repository. If no
   * record is found, a {@link ResourceNotFoundException} is thrown before any
   * deletion attempt is made.</p>
   *
   * @param id the {@link UUID} of the faculty member to delete
   * @throws ResourceNotFoundException    if no faculty record exists with the given {@code id}
   * @throws FacultyNotDeletedException   if a domain-level deletion constraint is violated
   * @throws InternalServerErrorException if any unexpected error occurs during deletion
   */
  @Override
  public void deleteFaculty(UUID id) {
    try {
      Faculty faculty =
          facultyRepository
              .findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("faculty not found with id: " + id));
      facultyRepository.delete(faculty);
    } catch (FacultyNotDeletedException e) {
      log.error("Error deleting faculty {}: {}", id, e.getMessage());
      throw new FacultyNotDeletedException("Failed to delete faculty record");
    } catch (Exception e) {
      log.error("Unexpected error occurred while deleting faculty {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while deleting the faculty record");
    }
  }
}
