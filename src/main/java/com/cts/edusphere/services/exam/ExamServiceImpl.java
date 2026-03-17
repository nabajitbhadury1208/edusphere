package com.cts.edusphere.services.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.CourseNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ExamNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.ExamCouldNotBeDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.ExamCouldNotBeUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.ExamNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ExamsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.exam.ExamMapper;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.exam.ExamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;

    @Override
    public ExamResponse createExam(ExamRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + request.courseId()));

        try {
            Exam exam = ExamMapper.toEntity(request, course);
            Exam savedExam = examRepository.save(exam);

            return ExamMapper.toDTO(savedExam);
        } catch (ExamNotCreatedException e) {
            log.error("Error occurred while creating exam: {}", e.getMessage());
            throw new ExamNotCreatedException("Could not create exam: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating exam: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while creating the exam: " + e.getMessage());
        }
    }

    @Override
    public List<ExamResponse> getAllExams() {
        try {
            return examRepository.findAll()
                    .stream()
                    .map(ExamMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (ExamsNotFoundException e) {
            log.error("Error occurred while fetching exams: {}", e.getMessage());
            throw new ExamsNotFoundException("No exams found");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching exams: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while fetching exams: " + e.getMessage());
        }
    }

    @Override
    public ExamResponse getExamById(UUID id) {
        try {
            Exam exam = examRepository.findById(id)
                    .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + id));
    
            return ExamMapper.toDTO(exam);

        } catch (ExamNotFoundException e) {
            log.error("Error occurred while fetching exam with id {}: {}", id, e.getMessage());
            throw new ExamNotFoundException("Exam not found with id: " + id);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching exam with id {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while fetching the exam: " + e.getMessage());
        }
    }

    @Override
    public ExamResponse updateExam(UUID id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + id));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        try {
            exam.setCourse(course);
            exam.setType(request.type());
            exam.setDate(request.date());
            exam.setStatus(request.status());

            Exam updatedExam = examRepository.save(exam);
            return ExamMapper.toDTO(updatedExam);
        } catch (ExamCouldNotBeUpdatedException e) {
            throw new ExamCouldNotBeUpdatedException("Could not update exam: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating exam with id {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the exam: " + e.getMessage());
        }
    }

    @Override
    public ExamResponse updateExamStatus(UUID id, Status status) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + id));

        try {
            exam.setStatus(status);
            Exam updatedExam = examRepository.save(exam);

            return ExamMapper.toDTO(updatedExam);
        } catch (ExamCouldNotBeUpdatedException e) {
            throw new ExamCouldNotBeUpdatedException("Could not update exam status: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating exam status with id {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the exam status: " + e.getMessage());
        }
    }

    @Override
    public void deleteExam(UUID id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + id));

        try {
            examRepository.delete(exam);
        } catch (ExamCouldNotBeDeletedException e) {
            throw new ExamCouldNotBeDeletedException("Cannot delete the exam: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting exam with id {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while deleting the exam: " + e.getMessage());
        }
    } 

    @Override
    public List<ExamResponse> getExamsByCourse(UUID courseId) {
        try {
            return examRepository.findByCourseId(courseId)
                    .stream()
                    .map(ExamMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (ExamsNotFoundException e) {
            throw new ExamsNotFoundException("No exams found for course with id: " + courseId);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching exams for course with id {}: {}", courseId, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while fetching exams for the course: " + e.getMessage());
        }
    }
}