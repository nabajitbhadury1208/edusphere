package com.cts.edusphere.controllers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createExam(@RequestBody ExamRequest request){
        try{
            ExamResponse response = examService.createExam(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);

        }catch (Exception ex){
            return new ResponseEntity<>("Unable to create exam", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllExams(){
        try{
            List<ExamResponse> exams=examService.getAllExams();
            return ResponseEntity.ok(exams);
        }catch (Exception ex){
            return new ResponseEntity<>("Error retrieving exams",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getExamById(@PathVariable UUID id){
        try{
            ExamResponse exam=examService.getExamById(id);
            return ResponseEntity.ok(exam);
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExam(@PathVariable UUID id){
        try{
           examService.deleteExam(id);
            return ResponseEntity.noContent().build();
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getExamsByCourse(@PathVariable UUID courseId){
        try{
            List<ExamResponse> exams=examService.getExamsByCourse(courseId);
            return ResponseEntity.ok(exams);
        }catch (Exception ex){
            return new ResponseEntity<>("Error retrieving exams for course",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
