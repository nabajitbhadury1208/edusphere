package com.cts.edusphere.controllers.grade;
import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.services.grade.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<?> createGrade(@RequestBody GradeRequest request) {
        try {
            GradeResponse response = gradeService.createGrade(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            return new ResponseEntity<>("Unable to create grade", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGrades() {
        try {
            List<GradeResponse> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);

        } catch (Exception ex) {
            return new ResponseEntity<>("Error retrieving grades", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGradeById(@PathVariable UUID id) {
        try {
            GradeResponse grade = gradeService.getGradeById(id);
            return ResponseEntity.ok(grade);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable UUID id, @RequestBody GradeRequest request) {
        try {
            GradeResponse updatedGrade = gradeService.updateGrade(id, request);
            return ResponseEntity.ok(updatedGrade);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable UUID id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<?> getGradesByStudent(@PathVariable UUID studentId) {
        try {
            List<GradeResponse> grades = gradeService.getGradesByStudent(studentId);
            return ResponseEntity.ok(grades);

        } catch (Exception ex) {
            return new ResponseEntity<>("Error retrieving grades for student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<?> getGradesByExam(@PathVariable UUID examId) {
        try {
            List<GradeResponse> grades = gradeService.getGradesByExam(examId);
            return ResponseEntity.ok(grades);

        } catch (Exception ex) {
            return new ResponseEntity<>("Error retrieving grades for exam", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
