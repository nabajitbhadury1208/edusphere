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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;
  private final DepartmentMapper departmentMapper;

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
