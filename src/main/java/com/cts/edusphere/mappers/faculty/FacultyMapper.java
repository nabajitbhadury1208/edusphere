package com.cts.edusphere.mappers.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.modules.faculty.Faculty;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapper {

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