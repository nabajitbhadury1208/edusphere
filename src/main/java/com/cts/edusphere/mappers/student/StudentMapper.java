package com.cts.edusphere.mappers.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

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