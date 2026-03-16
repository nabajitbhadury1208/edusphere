package com.cts.edusphere.controllers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
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
public class ExamController {

  private final ExamService examService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
  public ResponseEntity<ExamResponse> createExam(@Validated(OnCreate.class) @RequestBody ExamRequest request) {

    ExamResponse response = examService.createExam(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);

  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','DEPARTMENT_HEAD)")
  public ResponseEntity<List<ExamResponse>> getAllExams() {

    List<ExamResponse> exams = examService.getAllExams();
    return ResponseEntity.ok(exams);

  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
  public ResponseEntity<ExamResponse> updateExamById(@PathVariable UUID id,
      @Validated(OnUpdate.class) @RequestBody ExamRequest request) {
    ExamResponse updatedExam = examService.updateExam(id, request);
    return ResponseEntity.ok(updatedExam);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
  public ResponseEntity<ExamResponse> getExamById(@PathVariable UUID id) {

    ExamResponse exam = examService.getExamById(id);
    return ResponseEntity.ok(exam);

  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ExamResponse> deleteExam(@PathVariable UUID id) {

    examService.deleteExam(id);
    return ResponseEntity.noContent().build();

  }

  @GetMapping("/course/{courseId}")
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
  public ResponseEntity<List<ExamResponse>> getExamsByCourse(@PathVariable UUID courseId) {

    List<ExamResponse> exams = examService.getExamsByCourse(courseId);
    return ResponseEntity.ok(exams);
  }
}
