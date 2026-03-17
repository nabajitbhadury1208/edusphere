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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FacultyServiceImpl implements FacultyService {

  private final FacultyRepository facultyRepository;
  private final DepartmentRepository departmentRepository;
  private final FacultyMapper facultyMapper;
  private final PasswordEncoder passwordEncoder;

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
