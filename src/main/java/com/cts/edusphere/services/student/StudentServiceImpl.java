package com.cts.edusphere.services.student;

import com.cts.edusphere.common.dto.StudentRequestDTO;
import com.cts.edusphere.common.dto.StudentResponseDTO;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.UserMapper;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        Student student = userMapper.toStudentEntity(requestDTO);
        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Student savedStudent = studentRepository.save(student);
        return userMapper.toStudentResponseDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return userMapper.toStudentResponseDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(userMapper::toStudentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setName(requestDTO.name());
        student.setEmail(requestDTO.email());
        student.setPhone(requestDTO.phone());
        student.setPassword(passwordEncoder.encode(requestDTO.password()));
        student.setDob(requestDTO.dob());
        student.setGender(requestDTO.gender());
        student.setAddress(requestDTO.address());
        student.setStatus(requestDTO.status());

        Student updatedStudent = studentRepository.save(student);
        return userMapper.toStudentResponseDTO(updatedStudent);
    }

    @Override
    public StudentResponseDTO partiallyUpdateStudent(UUID id, StudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (requestDTO.name() != null) {
            student.setName(requestDTO.name());
        }
        if (requestDTO.email() != null) {
            student.setEmail(requestDTO.email());
        }
        if (requestDTO.phone() != null) {
            student.setPhone(requestDTO.phone());
        }
        if (requestDTO.password() != null) {
            student.setPassword(passwordEncoder.encode(requestDTO.password()));
        }
        if (requestDTO.dob() != null) {
            student.setDob(requestDTO.dob());
        }
        if (requestDTO.gender() != null) {
            student.setGender(requestDTO.gender());
        }
        if (requestDTO.address() != null) {
            student.setAddress(requestDTO.address());
        }
        if (requestDTO.status() != null) {
            student.setStatus(requestDTO.status());
        }

        Student updatedStudent = studentRepository.save(student);
        return userMapper.toStudentResponseDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }
}
