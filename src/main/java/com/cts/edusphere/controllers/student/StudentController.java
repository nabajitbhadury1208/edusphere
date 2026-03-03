package com.cts.edusphere.controllers.student;

import com.cts.edusphere.common.dto.StudentRequestDTO;
import com.cts.edusphere.common.dto.StudentResponseDTO;
import com.cts.edusphere.services.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO responseDTO = studentService.createStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID id) {
        StudentResponseDTO responseDTO = studentService.getStudentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO responseDTO = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> partiallyUpdateStudent(
            @PathVariable UUID id,
            @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO responseDTO = studentService.partiallyUpdateStudent(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}

