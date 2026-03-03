package com.cts.edusphere.services.student;

import com.cts.edusphere.common.dto.StudentRequestDTO;
import com.cts.edusphere.common.dto.StudentResponseDTO;
import com.cts.edusphere.modules.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);
    StudentResponseDTO getStudentById(UUID id);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO);
    StudentResponseDTO partiallyUpdateStudent(UUID id, StudentRequestDTO requestDTO);
    void deleteStudent(UUID id);
}

