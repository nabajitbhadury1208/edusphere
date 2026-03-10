package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.DepartmentMapper;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.DepartmentHead;
import com.cts.edusphere.repositories.DepartmentHeadRepository;
import com.cts.edusphere.repositories.DepartmentRepository;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentHeadRepository departmentHeadRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        try {
            Department department = departmentMapper.toEntity(requestDTO);

            if (requestDTO.headId() != null) {
                DepartmentHead head = departmentHeadRepository.findById(requestDTO.headId())
                        .orElseThrow(() -> new ResourceNotFoundException("department Head not found with id: " + requestDTO.headId()));
                department.setHead(head);
            }

            Department savedDepartment = departmentRepository.save(department);
            log.info("department created successfully: {}", savedDepartment.getDepartmentName());
            return departmentMapper.toResponseDTO(savedDepartment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating department: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create department");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(UUID id) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("department not found with id: " + id));
            return departmentMapper.toResponseDTO(department);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving department {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve department");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        try {
            return departmentRepository.findAll().stream()
                    .map(departmentMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving all departments: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve departments list");
        }
    }



    @Override
    public DepartmentResponseDTO updateDepartment(UUID id, DepartmentRequestDTO requestDTO) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("department not found with id: " + id));

            if (requestDTO.departmentName() != null) department.setDepartmentName(requestDTO.departmentName());
            if (requestDTO.departmentCode() != null) department.setDepartmentCode(requestDTO.departmentCode());
            if (requestDTO.contactInfo() != null) department.setContactInfo(requestDTO.contactInfo());
            if (requestDTO.status() != null) department.setStatus(requestDTO.status());

            if (requestDTO.headId() != null) {
                DepartmentHead head = departmentHeadRepository.findById(requestDTO.headId())
                        .orElseThrow(() -> new ResourceNotFoundException("department Head not found with id: " + requestDTO.headId()));
                department.setHead(head);
            }

            Department updatedDepartment = departmentRepository.save(department);
            log.info("department partially updated: {}", id);
            return departmentMapper.toResponseDTO(updatedDepartment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error partially updating department {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to partially update department");
        }
    }

    @Override
    public DepartmentResponseDTO changeDepartmentHead(UUID id, UUID headId) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("department not found with id: " + id));

            DepartmentHead head = departmentHeadRepository.findById(headId)
                    .orElseThrow(() -> new ResourceNotFoundException("department Head not found with id: " + headId));

            department.setHead(head);
            Department updatedDepartment = departmentRepository.save(department);
            log.info("department head changed for department: {}", id);
            return departmentMapper.toResponseDTO(updatedDepartment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error changing department head for {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to change department head");
        }
    }

    @Override
    public void deleteDepartment(UUID id) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("department not found with id: " + id));
            departmentRepository.delete(department);
            log.info("department deleted: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting department {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete department");
        }
    }
}