package com.cts.edusphere.controllers.student;

import com.cts.edusphere.common.dto.Student.StudentRequestDTO;
import com.cts.edusphere.common.dto.Student.StudentResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        try {
            StudentResponseDTO responseDTO = studentService.createStudent(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            log.error("Error creating student: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        try {
            List<StudentResponseDTO> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            log.error("Error fetching all students: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY') or (hasRole('STUDENT') and #id == principal.userId)")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID id) {
        try {
            StudentResponseDTO responseDTO = studentService.getStudentById(id);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Student not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching student {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        try {
            StudentResponseDTO responseDTO = studentService.updateStudent(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Update failed - Student not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating student {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> partiallyUpdateStudent(
            @PathVariable UUID id,
            @RequestBody StudentRequestDTO requestDTO) {
        try {
            StudentResponseDTO responseDTO = studentService.partiallyUpdateStudent(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Partial update failed - Student not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error partially updating student {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Delete failed - Student not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting student {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}