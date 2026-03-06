package com.cts.edusphere.services.exam;


import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.mappers.ExamMapper;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Exam;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.ExamRepository;
import com.mysql.cj.CoreSession;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;



    @Override
    public ExamResponse createExam(ExamRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() ->new RuntimeException("Course not found"));

        Exam exam = ExamMapper.toEntity(request, course);
        Exam savedExam = examRepository.save(exam);

        return ExamMapper.toDTO(savedExam);
    }

    @Override
    public List<ExamResponse> getAllExams(){
        return examRepository.findAll()
                .stream()
                .map(ExamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(UUID id) {
        Exam exam=examRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Exam not found"));

        return ExamMapper.toDTO(exam);
    }

    @Override
    public ExamResponse updateExam(UUID id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Exam not found"));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(()-> new RuntimeException("Course not found"));

        exam.setCourse(course);
        exam.setType(request.type());
        exam.setDate(request.date());
        exam.setStatus(request.status());

        Exam updatedExam =examRepository.save(exam);
        return ExamMapper.toDTO(updatedExam);
    }

    @Override
    public ExamResponse partialUpdateExam(UUID id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Exam not found"));

        if(request.courseId() != null){
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            exam.setCourse(course);
        }

        if(request.type() !=null){
            exam.setType(request.type());
        }
        if(request.date() !=null){
            exam.setDate(request.date());
        }
        if(request.status() !=null){
            exam.setStatus(request.status());
        }
        Exam updatedExam=examRepository.save(exam);

        return ExamMapper.toDTO(updatedExam);

    }

    @Override
    public ExamResponse updateExamStatus(UUID id, Status status) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Exam not found"));

        exam.setStatus(status);

        Exam updatedExam = examRepository.save(exam);

        return ExamMapper.toDTO(updatedExam);
    }

    @Override
    public void deleteExam(UUID id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Exam not found"));

        examRepository.delete(exam);

    }

    @Override
    public List<ExamResponse> getExamsByCourse(UUID courseId) {
        return examRepository.findByCourseId(courseId)
                .stream()
                .map(ExamMapper::toDTO)
                .collect(Collectors.toList());
    }



}
