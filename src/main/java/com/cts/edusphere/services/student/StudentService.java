package com.cts.edusphere.services.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for student management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting student records.
 * All operations use Data Transfer Objects (DTOs) to decouple the service layer from
 * the underlying persistence model.</p>
 */
public interface StudentService {

    /**
     * Creates a new student record from the provided request data.
     *
     * @param requestDTO the {@link StudentRequestDTO} containing the student's details; must not be {@code null}
     * @return a {@link StudentResponseDTO} representing the newly created student record
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a student with the same identifying information already exists
     */
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    /**
     * Retrieves a single student record by its unique identifier.
     *
     * @param id the {@link UUID} of the student to retrieve
     * @return a {@link StudentResponseDTO} representing the found student record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    StudentResponseDTO getStudentById(UUID id);

    /**
     * Retrieves all student records in the system.
     *
     * @return a {@link List} of {@link StudentResponseDTO} objects representing all students;
     *         never {@code null}, may be empty
     */
    List<StudentResponseDTO> getAllStudents();

    /**
     * Updates the details of an existing student record.
     *
     * @param id         the {@link UUID} of the student to update
     * @param requestDTO the {@link StudentRequestDTO} containing the updated field values; must not be {@code null}
     * @return a {@link StudentResponseDTO} representing the updated student record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO);

    /**
     * Deletes the student record identified by the given ID.
     *
     * @param id the {@link UUID} of the student to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    void deleteStudent(UUID id);
}
