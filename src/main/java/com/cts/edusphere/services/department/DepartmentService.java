package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.DepartmentResponseDTO;
import com.cts.edusphere.modules.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);
    DepartmentResponseDTO getDepartmentById(UUID id);
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO updateDepartment(UUID id, DepartmentRequestDTO requestDTO);
    DepartmentResponseDTO partiallyUpdateDepartment(UUID id, DepartmentRequestDTO requestDTO);
    DepartmentResponseDTO changeDepartmentHead(UUID id, UUID headId);
    void deleteDepartment(UUID id);
}

