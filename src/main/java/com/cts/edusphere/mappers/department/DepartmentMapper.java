package com.cts.edusphere.mappers.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.modules.department.Department;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Department} entity objects
 * and their corresponding DTO representations ({@link DepartmentRequestDTO} and
 * {@link DepartmentResponseDTO}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever department
 * mapping is required.</p>
 */
@Component
public class DepartmentMapper {

    /**
     * Converts a {@link Department} entity to a {@link DepartmentResponseDTO}.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The department head's ID and name are extracted safely; if no department head
     * is assigned, both fields will be {@code null} in the response.</p>
     *
     * @param department the {@link Department} entity to convert; may be {@code null}
     * @return a {@link DepartmentResponseDTO} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
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

    /**
     * Converts a {@link DepartmentRequestDTO} to a {@link Department} entity.
     *
     * <p>If the provided DTO is {@code null}, this method returns {@code null}.
     * Note that the department head association is not set here and must be assigned
     * separately after entity creation.</p>
     *
     * @param dto the {@link DepartmentRequestDTO} containing the data to map; may be {@code null}
     * @return a new {@link Department} entity built from the DTO data,
     *         or {@code null} if the input is {@code null}
     */
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
