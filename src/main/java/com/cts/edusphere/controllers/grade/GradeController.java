package com.cts.edusphere.controllers.grade;
import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.grade.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createGrade(@Validated(OnCreate.class) @RequestBody GradeRequest request) {
            GradeResponse response = gradeService.createGrade(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllGrades() {
            List<GradeResponse> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGradeById(@PathVariable UUID id) {
            GradeResponse grade = gradeService.getGradeById(id);
            return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGrade(@PathVariable UUID id,@Validated(OnUpdate.class) @RequestBody GradeRequest request) {
            GradeResponse updatedGrade = gradeService.updateGrade(id, request);
            return ResponseEntity.ok(updatedGrade);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGrade(@PathVariable UUID id) {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGradesByStudent(@PathVariable UUID studentId) {
            List<GradeResponse> grades = gradeService.getGradesByStudent(studentId);
            return ResponseEntity.ok(grades);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGradesByExam(@PathVariable UUID examId) {
            List<GradeResponse> grades = gradeService.getGradesByExam(examId);
            return ResponseEntity.ok(grades);
    }
}
