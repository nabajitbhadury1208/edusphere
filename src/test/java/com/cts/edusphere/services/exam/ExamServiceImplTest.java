package com.cts.edusphere.services.exam;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.exam.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ExamServiceImpl examService;

    private Exam exam;
    private Course course;
    private ExamRequest examRequest;
    private UUID examId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        examId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Database Systems")
                .build();

        examRequest = new ExamRequest(courseId, ExamType.FINAL, LocalDate.now().plusDays(10), Status.ACTIVE);

        exam = Exam.builder()
                .id(examId)
                .course(course)
                .type(ExamType.FINAL)
                .date(LocalDate.now().plusDays(10))
                .status(Status.ACTIVE)
                .build();

        exam.setCreatedAt(Instant.now());
        exam.setUpdatedAt(Instant.now());
    }

    @Test
    void createExam_ShouldSucceed_WhenCourseExists() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ExamResponse result = examService.createExam(examRequest);

        assertNotNull(result);
        assertEquals(ExamType.FINAL, result.type());
        verify(courseRepository).findById(courseId);
        verify(examRepository).save(any(Exam.class));
    }

    @Test
    void createExam_ShouldThrowResourceNotFound_WhenCourseDoesNotExist() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> examService.createExam(examRequest));
        verify(examRepository, never()).save(any());
    }
    @Test
    void getAllExams_ShouldReturnListOfResponses_WhenExamsExist() {
        when(examRepository.findAll()).thenReturn(List.of(exam));

        List<ExamResponse> result = examService.getAllExams();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(examId, result.get(0).id());
        verify(examRepository).findAll();
    }
    @Test
    void getExamById_ShouldReturnResponse_WhenIdExists() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        ExamResponse result = examService.getExamById(examId);

        assertNotNull(result);
        assertEquals(examId, result.id());
        verify(examRepository).findById(examId);
    }
    @Test
    void getExamById_ShouldThrowResourceNotFound_WhenIdDoesNotExist() {
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> examService.getExamById(examId));
        verify(examRepository).findById(examId);
    }
    @Test
    void updateExam_ShouldUpdateFields_WhenExamAndCourseExist() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ExamResponse result = examService.updateExam(examId, examRequest);

        assertNotNull(result);
        verify(examRepository).save(exam);
    }
    @Test
    void updateExam_ShouldThrowResourceNotFound_WhenCourseNotFound() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> examService.updateExam(examId, examRequest));
        verify(examRepository, never()).save(any());
    }
    @Test
    void updateExamStatus_ShouldChangeStatus_WhenExamExists() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ExamResponse result = examService.updateExamStatus(examId, Status.INACTIVE);

        assertNotNull(result);
        assertEquals(Status.INACTIVE, exam.getStatus());
        verify(examRepository).save(exam);
    }

    @Test
    void deleteExam_ShouldInvokeDelete_WhenExists() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        examService.deleteExam(examId);

        verify(examRepository).delete(exam);
    }

    @Test
    void deleteExam_ShouldThrowCannotDeleteException_WhenRepositoryFails() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        doThrow(new RuntimeException("DB Error")).when(examRepository).delete(exam);

        assertThrows(CannotDeleteException.class, () -> examService.deleteExam(examId));
    }

    @Test
    void getExamsByCourse_ShouldReturnList() {
        when(examRepository.findByCourseId(courseId)).thenReturn(List.of(exam));

        List<ExamResponse> result = examService.getExamsByCourse(courseId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(examRepository).findByCourseId(courseId);
    }
}