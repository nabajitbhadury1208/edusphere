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
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<GradeResponse> createGrade(@Validated(OnCreate.class) @RequestBody GradeRequest request) {
            GradeResponse response = gradeService.createGrade(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
            List<GradeResponse> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','COMPLIANCE')")
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable UUID id) {
            GradeResponse grade = gradeService.getGradeById(id);
            return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<GradeResponse> updateGrade(@PathVariable UUID id,@Validated(OnUpdate.class) @RequestBody GradeRequest request) {
            GradeResponse updatedGrade = gradeService.updateGrade(id, request);
            return ResponseEntity.ok(updatedGrade);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradeResponse> deleteGrade(@PathVariable UUID id) {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','DEPARTMENT_HEAD','STUDENT','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(@PathVariable UUID studentId) {
            List<GradeResponse> grades = gradeService.getGradesByStudent(studentId);
            return ResponseEntity.ok(grades);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getGradesByExam(@PathVariable UUID examId) {
            List<GradeResponse> grades = gradeService.getGradesByExam(examId);
            return ResponseEntity.ok(grades);
    }
}
