package com.cts.edusphere.mappers.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.modules.faculty.Faculty;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Faculty} entity objects
 * and their corresponding DTO representations ({@link FacultyRequestDTO} and
 * {@link FacultyResponseDTO}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever faculty
 * mapping is required.</p>
 */
@Component
public class FacultyMapper {

    /**
     * Converts a {@link Faculty} entity to a {@link FacultyResponseDTO}.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The department ID and name are extracted safely; if no department is assigned,
     * both fields will be {@code null} in the response.</p>
     *
     * @param faculty the {@link Faculty} entity to convert; may be {@code null}
     * @return a {@link FacultyResponseDTO} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public FacultyResponseDTO toResponseDTO(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        return new FacultyResponseDTO(
                faculty.getId(),
                faculty.getName(),
                faculty.getEmail(),
                faculty.getPhone(),
                faculty.getRoles(),
                faculty.getStatus(),
                faculty.getPosition(),
                faculty.getDepartment() != null ? faculty.getDepartment().getId() : null,
                faculty.getDepartment() != null ? faculty.getDepartment().getDepartmentName() : null,
                faculty.getJoinDate(),
                faculty.getCreatedAt(),
                faculty.getUpdatedAt()
        );
    }

    /**
     * Converts a {@link FacultyRequestDTO} to a {@link Faculty} entity.
     *
     * <p>If the provided DTO is {@code null}, this method returns {@code null}.
     * Note that the department association is not set here and must be assigned
     * separately after entity creation.</p>
     *
     * @param dto the {@link FacultyRequestDTO} containing the data to map; may be {@code null}
     * @return a new {@link Faculty} entity built from the DTO data,
     *         or {@code null} if the input is {@code null}
     */
    public Faculty toEntity(FacultyRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Faculty.builder()
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .password(dto.password())
                .position(dto.position())
                .status(dto.status())
                .build();
    }
}
