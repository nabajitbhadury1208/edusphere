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

/**
 * Implementation of {@link StudentService} that provides CRUD operations
 * for managing student records in the EduSphere platform.
 *
 * <p>All public methods participate in Spring-managed transactions. Write
 * operations are audited via {@code @ComplianceAudit} where applicable.
 * Passwords are stored in encoded form using the configured
 * {@link org.springframework.security.crypto.password.PasswordEncoder}.</p>
 *
 * @see StudentService
 * @see com.cts.edusphere.modules.student.Student
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new student record in the system.
     *
     * <p>The incoming request is mapped to a {@link Student} entity, assigned the
     * {@code STUDENT} role, given {@code ACTIVE} status, and persisted with an
     * encoded password. This operation is compliance-audited for eligibility
     * and documentation verification.</p>
     *
     * @param requestDTO the data transfer object containing the new student's details;
     *                   must not be {@code null}
     * @return a {@link StudentResponseDTO} representing the persisted student record
     * @throws StudentCreationFailedException if a student-creation-specific error occurs
     * @throws InternalServerErrorException  if any other unexpected error occurs during persistence
     */
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

    /**
     * Retrieves a single student record by its unique identifier.
     *
     * <p>Executes within a read-only transaction. If no student is found for the
     * supplied {@code id}, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param id the {@link UUID} of the student to retrieve; must not be {@code null}
     * @return a {@link StudentResponseDTO} containing the student's details
     * @throws StudentNotFoundException      if no student exists with the given {@code id}
     * @throws InternalServerErrorException  if any unexpected error occurs during retrieval
     */
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

    /**
     * Retrieves all student records currently stored in the system.
     *
     * <p>Executes within a read-only transaction. Each {@link Student} entity is
     * mapped to a {@link StudentResponseDTO} before being returned.</p>
     *
     * @return a {@link List} of {@link StudentResponseDTO} objects representing every
     *         student; never {@code null}, but may be empty if no students exist
     * @throws StudentsNotFoundException    if a students-list-specific error occurs
     * @throws InternalServerErrorException if any unexpected error occurs during retrieval
     */
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

    /**
     * Updates an existing student record with the values supplied in the request DTO.
     *
     * <p>Only non-{@code null} fields in {@code requestDTO} are applied to the
     * existing entity, preserving any fields that are not included in the update
     * payload. The updated entity is then persisted and returned as a response DTO.</p>
     *
     * @param id         the {@link UUID} of the student to update; must not be {@code null}
     * @param requestDTO the data transfer object containing the fields to update;
     *                   {@code null} field values are ignored
     * @return a {@link StudentResponseDTO} reflecting the updated student record
     * @throws ResourceNotFoundException    if no student exists with the given {@code id}
     * @throws StudentUpdateFailedException if a student-update-specific error occurs
     * @throws InternalServerErrorException if any unexpected error occurs during the update
     */
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


    /**
     * Deletes the student record identified by the given {@code id}.
     *
     * <p>The student is first looked up by {@code id}; if found, the entity is
     * removed from the repository. If no student exists with the given {@code id},
     * a {@link ResourceNotFoundException} is thrown before any deletion is attempted.</p>
     *
     * @param id the {@link UUID} of the student to delete; must not be {@code null}
     * @throws ResourceNotFoundException      if no student exists with the given {@code id}
     * @throws StudentDeletionFailedException if a student-deletion-specific error occurs
     * @throws InternalServerErrorException   if any unexpected error occurs during deletion
     */
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