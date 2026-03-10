package com.cts.edusphere.services.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);
    StudentResponseDTO getStudentById(UUID id);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO);
    void deleteStudent(UUID id);
}

