package com.cts.edusphere.controllers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
/**
 * REST controller for managing academic exams.
 * Base path: /api/v1/exams
 */
public class ExamController {

  private final ExamService examService;

  /**
   * Creates a new exam linked to a specific course.
   * Accessible by ADMIN and FACULTY roles.
   *
   * @param request the request containing course ID, exam type, date, and status
   * @return HTTP 201 with the created ExamResponse
   */
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
  public ResponseEntity<ExamResponse> createExam(@Validated(OnCreate.class) @RequestBody ExamRequest request) {

    ExamResponse response = examService.createExam(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);

  }

  /**
   * Retrieves all exams in the system.
   * Accessible by ADMIN, FACULTY, and DEPARTMENT_HEAD roles.
   *
   * @return HTTP 200 with a list of all ExamResponse objects
   */
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','DEPARTMENT_HEAD')")
  public ResponseEntity<List<ExamResponse>> getAllExams() {

    List<ExamResponse> exams = examService.getAllExams();
    return ResponseEntity.ok(exams);

  }

  /**
   * Updates an existing exam's details by its unique identifier.
   * Accessible by ADMIN and FACULTY roles.
   *
   * @param id      the UUID of the exam to update
   * @param request the request body containing updated exam fields
   * @return HTTP 200 with the updated ExamResponse
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
  public ResponseEntity<ExamResponse> updateExamById(@PathVariable UUID id,
      @Validated(OnUpdate.class) @RequestBody ExamRequest request) {
    ExamResponse updatedExam = examService.updateExam(id, request);
    return ResponseEntity.ok(updatedExam);
  }

  /**
   * Retrieves a specific exam by its unique identifier.
   * Accessible by ADMIN, FACULTY, and STUDENT roles.
   *
   * @param id the UUID of the exam to retrieve
   * @return HTTP 200 with the matching ExamResponse
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
  public ResponseEntity<ExamResponse> getExamById(@PathVariable UUID id) {

    ExamResponse exam = examService.getExamById(id);
    return ResponseEntity.ok(exam);

  }

  /**
   * Permanently deletes an exam by its unique identifier.
   * Accessible by ADMIN role only.
   *
   * @param id the UUID of the exam to delete
   * @return HTTP 204 No Content on successful deletion
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ExamResponse> deleteExam(@PathVariable UUID id) {

    examService.deleteExam(id);
    return ResponseEntity.noContent().build();

  }

  /**
   * Retrieves all exams associated with a specific course.
   * Accessible by ADMIN, FACULTY, and STUDENT roles.
   *
   * @param courseId the UUID of the course whose exams are to be fetched
   * @return HTTP 200 with a list of ExamResponse objects for the given course
   */
  @GetMapping("/course/{courseId}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
  public ResponseEntity<List<ExamResponse>> getExamsByCourse(@PathVariable UUID courseId) {

    List<ExamResponse> exams = examService.getExamsByCourse(courseId);
    return ResponseEntity.ok(exams);
  }
}
