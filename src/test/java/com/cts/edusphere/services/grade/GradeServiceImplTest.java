package com.cts.edusphere.services.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.enums.GradeStatus;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.modules.grade.Grade;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.exam.ExamRepository;
import com.cts.edusphere.repositories.grade.GradeRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GradeServiceImplTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GradeServiceImpl gradeService;

    private UUID gradeId;
    private UUID studentId;
    private UUID examId;
    private Grade grade;
    private GradeRequest gradeRequest;
    private Student student;
    private Exam exam;

    @BeforeEach
    void setUp() {
        gradeId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        examId = UUID.randomUUID();

        student = new Student();
        student.setId(studentId);

        exam = new Exam();
        exam.setId(examId);

        grade = Grade.builder()
                .id(gradeId)
                .student(student)
                .exam(exam)
                .score(90.0)
                .grade("A")
                .status(GradeStatus.PASS)
                .build();
        grade.setCreatedAt(Instant.now());
        grade.setUpdatedAt(Instant.now());

        gradeRequest = new GradeRequest(
                examId,
                studentId,
                90.0,
                "A",
                GradeStatus.PASS
        );
    }

    @Test
    void testCreateGrade_Success() {
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        GradeResponse result = gradeService.createGrade(gradeRequest);

        assertNotNull(result);
        assertEquals(90.0, result.score());
        assertEquals(studentId, result.studentId());
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void testCreateGrade_ExamNotFound() {
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.createGrade(gradeRequest));
        verify(gradeRepository, never()).save(any());
    }

    @Test
    void testGetGradeById_Success() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));

        GradeResponse result = gradeService.getGradeById(gradeId);

        assertNotNull(result);
        assertEquals("A", result.grade());
        verify(gradeRepository, times(1)).findById(gradeId);
    }

    @Test
    void testGetAllGrades_Success() {
        when(gradeRepository.findAll()).thenReturn(List.of(grade));

        List<GradeResponse> result = gradeService.getAllGrades();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(90.0, result.get(0).score());
    }

    @Test
    void testUpdateGrade_Success() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        GradeResponse result = gradeService.updateGrade(gradeId, gradeRequest);

        assertNotNull(result);
        verify(gradeRepository, times(1)).save(grade);
    }

    @Test
    void testDeleteGrade_Success() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
        doNothing().when(gradeRepository).delete(grade);

        assertDoesNotThrow(() -> gradeService.deleteGrade(gradeId));
        verify(gradeRepository, times(1)).delete(grade);
    }

    @Test
    void testDeleteGrade_NotFound() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteGrade(gradeId));
        verify(gradeRepository, never()).delete(any());
    }

    @Test
    void testDeleteGrade_CannotDeleteException() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
        doThrow(new RuntimeException("Database constraint")).when(gradeRepository).delete(grade);

        assertThrows(CannotDeleteException.class, () -> gradeService.deleteGrade(gradeId));
    }

    @Test
    void testGetGradesByStudent_Success() {
        when(gradeRepository.findByStudentId(studentId)).thenReturn(List.of(grade));

        List<GradeResponse> result = gradeService.getGradesByStudent(studentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(studentId, result.get(0).studentId());
    }
}