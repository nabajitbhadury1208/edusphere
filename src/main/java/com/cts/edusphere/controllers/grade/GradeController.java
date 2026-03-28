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

/**
 * REST controller for managing student grades.
 * Base path: /api/v1/grades
 */
@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /**
     * Creates a new grade record for a student's exam.
     * Triggers a compliance audit upon successful creation.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param request the request containing student ID, exam ID, score, grade string, and status
     * @return HTTP 201 with the created GradeResponse
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<GradeResponse> createGrade(@Validated(OnCreate.class) @RequestBody GradeRequest request) {
            GradeResponse response = gradeService.createGrade(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves all grade records in the system.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @return HTTP 200 with a list of all GradeResponse objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
            List<GradeResponse> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);
    }

    /**
     * Retrieves a specific grade record by its unique identifier.
     * Accessible by ADMIN, FACULTY, and COMPLIANCE roles.
     *
     * @param id the UUID of the grade record to retrieve
     * @return HTTP 200 with the matching GradeResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','COMPLIANCE')")
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable UUID id) {
            GradeResponse grade = gradeService.getGradeById(id);
            return ResponseEntity.ok(grade);
    }

    /**
     * Updates an existing grade record by its unique identifier.
     * Triggers a compliance audit upon successful update.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id      the UUID of the grade to update
     * @param request the request body containing updated grade fields
     * @return HTTP 200 with the updated GradeResponse
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<GradeResponse> updateGrade(@PathVariable UUID id,@Validated(OnUpdate.class) @RequestBody GradeRequest request) {
            GradeResponse updatedGrade = gradeService.updateGrade(id, request);
            return ResponseEntity.ok(updatedGrade);
    }



    /**
     * Permanently deletes a grade record by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the grade to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradeResponse> deleteGrade(@PathVariable UUID id) {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all grade records associated with a specific student.
     * Accessible by ADMIN, FACULTY, DEPARTMENT_HEAD, STUDENT, and COMPLIANCE roles.
     *
     * @param studentId the UUID of the student whose grades are to be retrieved
     * @return HTTP 200 with a list of GradeResponse objects for the given student
     */
    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','DEPARTMENT_HEAD','STUDENT','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(@PathVariable UUID studentId) {
            List<GradeResponse> grades = gradeService.getGradesByStudent(studentId);
            return ResponseEntity.ok(grades);
    }

    /**
     * Retrieves all grade records associated with a specific exam.
     * Accessible by ADMIN, FACULTY, and COMPLIANCE roles.
     *
     * @param examId the UUID of the exam whose grades are to be retrieved
     * @return HTTP 200 with a list of GradeResponse objects for the given exam
     */
    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','COMPLIANCE')")
    public ResponseEntity<List<GradeResponse>> getGradesByExam(@PathVariable UUID examId) {
            List<GradeResponse> grades = gradeService.getGradesByExam(examId);
            return ResponseEntity.ok(grades);
    }
}
