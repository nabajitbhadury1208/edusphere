package com.cts.edusphere.controllers.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.student.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing student records.
 * Base path: /api/v1/students
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    /**
     * Creates a new student account.
     * Encodes password, assigns STUDENT role, sets ACTIVE status, and triggers compliance audit.
     * Accessible by ADMIN only.
     *
     * @param requestDTO student details including name, email, and password
     * @return HTTP 201 with the created StudentResponseDTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> createStudent(@Validated(OnCreate.class) @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO responseDTO = studentService.createStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    /**
     * Retrieves all students.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @return HTTP 200 with a list of all StudentResponseDTO objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);

    }

    /**
     * Retrieves a student by their unique identifier.
     * Accessible by ADMIN and FACULTY roles, or the student themselves.
     *
     * @param id the UUID of the student to retrieve
     * @return HTTP 200 with the matching StudentResponseDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY') or (hasRole('STUDENT') and #id == principal.userId)")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID id) {
        StudentResponseDTO responseDTO = studentService.getStudentById(id);
        return ResponseEntity.ok(responseDTO);

    }

    /**
     * Updates student fields (partial update).
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the student to update
     * @param requestDTO the updated student fields
     * @return HTTP 200 with the updated StudentResponseDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO responseDTO = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    /**
     * Permanently deletes a student.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the student to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}