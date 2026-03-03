package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.*;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Student Mapping
    public StudentResponseDTO toStudentResponseDTO(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getRole(),
                student.getStatus(),
                student.getDob(),
                student.getGender(),
                student.getAddress(),
                student.getEnrollmentDate(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    public Student toStudentEntity(StudentRequestDTO dto) {
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
                .status(dto.status())
                .build();
    }

    // Faculty Mapping
    public FacultyResponseDTO toFacultyResponseDTO(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        return new FacultyResponseDTO(
                faculty.getId(),
                faculty.getName(),
                faculty.getEmail(),
                faculty.getPhone(),
                faculty.getRole(),
                faculty.getStatus(),
                faculty.getPosition(),
                faculty.getDepartment() != null ? faculty.getDepartment().getId() : null,
                faculty.getDepartment() != null ? faculty.getDepartment().getDepartmentName() : null,
                faculty.getJoinDate(),
                faculty.getCreatedAt(),
                faculty.getUpdatedAt()
        );
    }

    public Faculty toFacultyEntity(FacultyRequestDTO dto) {
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

    // Department Mapping
    public DepartmentResponseDTO toDepartmentResponseDTO(Department department) {
        if (department == null) {
            return null;
        }
        return new DepartmentResponseDTO(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentCode(),
                department.getContactInfo(),
                department.getStatus(),
                department.getHead() != null ? department.getHead().getId() : null,
                department.getHead() != null ? department.getHead().getName() : null,
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }

    public Department toDepartmentEntity(DepartmentRequestDTO dto) {
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
