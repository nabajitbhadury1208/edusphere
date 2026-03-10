package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.modules.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponseDTO toResponseDTO(Department department) {
        if (department == null) {
            return null;
        }
        return new DepartmentResponseDTO(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentCode(),
                department.getContactInfo(),
                department.getStatus(),
                department.getDepartmentHead() != null ? department.getDepartmentHead().getId() : null,
                department.getDepartmentHead() != null ? department.getDepartmentHead().getName() : null,
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }

    public Department toEntity(DepartmentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Department.builder()
                .departmentName(dto.departmentName())
                .departmentCode(dto.departmentCode())
                .contactInfo(dto.contactInfo())
                .status(dto.status())
                .build();
    }
}