package com.cts.edusphere.services.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ExamMapper;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Exam;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.ExamRepository;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        try {
            Exam exam = ExamMapper.toEntity(request, course);
            Exam savedExam = examRepository.save(exam);
            return ExamMapper.toDTO(savedExam);
        } catch (Exception e) {
            throw new RuntimeException("Could not create exam: " + e.getMessage());
        }
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll()
                .stream()
                .map(ExamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(UUID id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));

        return ExamMapper.toDTO(exam);
    }

    @Override
    public ExamResponse updateExam(UUID id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        try {
            exam.setCourse(course);
            exam.setType(request.type());
            exam.setDate(request.date());
            exam.setStatus(request.status());

            Exam updatedExam = examRepository.save(exam);
            return ExamMapper.toDTO(updatedExam);
        } catch (Exception e) {
            throw new RuntimeException("Could not update exam: " + e.getMessage());
        }
    }

    @Override
    public ExamResponse updateExamStatus(UUID id, Status status) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));

        try {
            exam.setStatus(status);
            Exam updatedExam = examRepository.save(exam);
            return ExamMapper.toDTO(updatedExam);
        } catch (Exception e) {
            throw new RuntimeException("Could not update exam status: " + e.getMessage());
        }
    }

    @Override
    public void deleteExam(UUID id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));

        try {
            examRepository.delete(exam);
        } catch (Exception e) {
            throw new CannotDeleteException("Cannot delete the exam: " + e.getMessage());
        }
    }

    @Override
    public List<ExamResponse> getExamsByCourse(UUID courseId) {
        return examRepository.findByCourseId(courseId)
                .stream()
                .map(ExamMapper::toDTO)
                .collect(Collectors.toList());
    }
}