package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.DepartmentResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.UserMapper;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.DepartmentHead;
import com.cts.edusphere.repositories.DepartmentHeadRepository;
import com.cts.edusphere.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentHeadRepository departmentHeadRepository;
    private final UserMapper userMapper;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        Department department = userMapper.toDepartmentEntity(requestDTO);

        if (requestDTO.headId() != null) {
            DepartmentHead head = departmentHeadRepository.findById(requestDTO.headId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department Head not found with id: " + requestDTO.headId()));
            department.setHead(head);
        }

        Department savedDepartment = departmentRepository.save(department);
        return userMapper.toDepartmentResponseDTO(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return userMapper.toDepartmentResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(userMapper::toDepartmentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO updateDepartment(UUID id, DepartmentRequestDTO requestDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        department.setDepartmentName(requestDTO.departmentName());
        department.setDepartmentCode(requestDTO.departmentCode());
        department.setContactInfo(requestDTO.contactInfo());
        department.setStatus(requestDTO.status());

        if (requestDTO.headId() != null) {
            DepartmentHead head = departmentHeadRepository.findById(requestDTO.headId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department Head not found with id: " + requestDTO.headId()));
            department.setHead(head);
        }

        Department updatedDepartment = departmentRepository.save(department);
        return userMapper.toDepartmentResponseDTO(updatedDepartment);
    }

    @Override
    public DepartmentResponseDTO partiallyUpdateDepartment(UUID id, DepartmentRequestDTO requestDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (requestDTO.departmentName() != null) {
            department.setDepartmentName(requestDTO.departmentName());
        }
        if (requestDTO.departmentCode() != null) {
            department.setDepartmentCode(requestDTO.departmentCode());
        }
        if (requestDTO.contactInfo() != null) {
            department.setContactInfo(requestDTO.contactInfo());
        }
        if (requestDTO.status() != null) {
            department.setStatus(requestDTO.status());
        }
        if (requestDTO.headId() != null) {
            DepartmentHead head = departmentHeadRepository.findById(requestDTO.headId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department Head not found with id: " + requestDTO.headId()));
            department.setHead(head);
        }

        Department updatedDepartment = departmentRepository.save(department);
        return userMapper.toDepartmentResponseDTO(updatedDepartment);
    }

    @Override
    public DepartmentResponseDTO changeDepartmentHead(UUID id, UUID headId) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        DepartmentHead head = departmentHeadRepository.findById(headId)
                .orElseThrow(() -> new ResourceNotFoundException("Department Head not found with id: " + headId));

        department.setHead(head);
        Department updatedDepartment = departmentRepository.save(department);
        return userMapper.toDepartmentResponseDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }
}
