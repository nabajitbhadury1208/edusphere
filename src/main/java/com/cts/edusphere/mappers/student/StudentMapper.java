package com.cts.edusphere.mappers.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Student} entity objects
 * and their corresponding DTO representations ({@link StudentRequestDTO} and
 * {@link StudentResponseDTO}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever student
 * mapping is required.</p>
 */
@Component
public class StudentMapper {

    /**
     * Converts a {@link Student} entity to a {@link StudentResponseDTO}.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * All personal and enrollment details are mapped directly from the entity fields.</p>
     *
     * @param student the {@link Student} entity to convert; may be {@code null}
     * @return a {@link StudentResponseDTO} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public StudentResponseDTO toResponseDTO(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getRoles(),
                student.getStatus(),
                student.getDob(),
                student.getGender(),
                student.getAddress(),
                student.getEnrollmentDate(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    /**
     * Converts a {@link StudentRequestDTO} to a {@link Student} entity.
     *
     * <p>If the provided DTO is {@code null}, this method returns {@code null}.
     * Note that enrollment-related fields such as {@code enrollmentDate}, roles, and
     * status are not set here and should be assigned separately after entity creation.</p>
     *
     * @param dto the {@link StudentRequestDTO} containing the data to map; may be {@code null}
     * @return a new {@link Student} entity built from the DTO data,
     *         or {@code null} if the input is {@code null}
     */
    public Student toEntity(StudentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Student.builder()
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .password(dto.password())
                .dob(dto.dob())
                .gender(dto.gender())
                .address(dto.address())
                .build();
    }
}
