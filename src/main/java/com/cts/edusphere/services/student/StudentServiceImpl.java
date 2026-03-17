package com.cts.edusphere.services.student;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentCreationFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDeletionFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentUpdateFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentsNotFoundException;
import com.cts.edusphere.mappers.student.StudentMapper;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @ComplianceAudit(entityType = AuditEntityType.STUDENT_CREATED, scope = "Verify new student eligibility and documentation")
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        try {
            Student student = studentMapper.toEntity(requestDTO);
            student.setRoles(Set.of(Role.STUDENT));
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            student.setStatus(Status.ACTIVE);

            Student savedStudent = studentRepository.save(student);
            log.info("student record created successfully with ID: {}", savedStudent.getId());
            return studentMapper.toResponseDTO(savedStudent);
        } catch (StudentCreationFailedException e) {
            log.error("Error occurred while creating student: {}", e.getMessage());
            throw new StudentCreationFailedException("Failed to create student record: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while creating student: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create student record");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(UUID id) {
        try {
            return studentRepository.findById(id)
                    .map(studentMapper::toResponseDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("student not found with id: " + id));
        }  catch (StudentNotFoundException e) {
            log.error("student with ID {} not found: {}", id, e.getMessage());
            throw new StudentNotFoundException("student with ID: " + id + " not found");

        } catch (Exception e) {
            log.error("Error occurred while fetching student {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve student details");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        try {
            return studentRepository.findAll().stream()
                    .map(studentMapper::toResponseDTO)
                    .collect(Collectors.toList());

        } catch (StudentsNotFoundException e) {
            log.error("Error occurred while fetching all students: {}", e.getMessage());
            throw new StudentsNotFoundException("Failed to retrieve students list");

        } catch (Exception e) {
            log.error("Error occurred while fetching all students: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve students list");
        }
    }

    @Override
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("student not found with id: " + id));

            if (requestDTO.name() != null) student.setName(requestDTO.name());
            if (requestDTO.email() != null) student.setEmail(requestDTO.email());
            if (requestDTO.phone() != null) student.setPhone(requestDTO.phone());

            if (requestDTO.dob() != null) student.setDob(requestDTO.dob());
            if (requestDTO.gender() != null) student.setGender(requestDTO.gender());
            if (requestDTO.address() != null) student.setAddress(requestDTO.address());

            Student updatedStudent = studentRepository.save(student);
            log.info("student record updated successfully: {}", id);
            return studentMapper.toResponseDTO(updatedStudent);

        } catch(StudentUpdateFailedException e) {
            log.error("Error occurred while updating student {}: {}", id, e.getMessage());
            throw new StudentUpdateFailedException("Failed to update student record: " + e.getMessage());

        } catch (Exception e) {
            log.error("Error occurred while updating student {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update student record");
        }
    }


    @Override
    public void deleteStudent(UUID id) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("student not found with id: " + id));
            studentRepository.delete(student);
            log.info("student record deleted successfully: {}", id);

        }catch(StudentDeletionFailedException e) {
            log.error("Error occurred while deleting student {}: {}", id, e.getMessage());
            throw new StudentDeletionFailedException("Failed to delete student record: " + e.getMessage());
            
        } catch (Exception e) {
            log.error("Error occurred while deleting student {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete student record");
        }
    }
}