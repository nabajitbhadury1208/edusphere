package com.cts.edusphere.services.grade;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ExamNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.GradeNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.GradeCouldNotBeDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.GradeNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.GradeNotUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.GradesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentNotFoundException;
import com.cts.edusphere.mappers.grade.GradeMapper;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.modules.grade.Grade;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.exam.ExamRepository;
import com.cts.edusphere.repositories.grade.GradeRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    @Override
    @ComplianceAudit(entityType = AuditEntityType.GRADE_ASSIGNED, scope = "Verify Grade assigned to Student")
    public GradeResponse createGrade(GradeRequest request) {
        try {
            Exam exam = examRepository.findById(request.examId())
                    .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + request.examId()));

            Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + request.studentId()));

            Grade grade = GradeMapper.toEntity(request, exam, student);
            Grade savedGrade = gradeRepository.save(grade);

            return GradeMapper.toDTO(savedGrade);
        } catch (GradeNotCreatedException e) {
            throw new GradeNotCreatedException("Could not create grade: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while creating the grade: " + e.getMessage());
        }
    }

    @Override
    public List<GradeResponse> getAllGrades() {
        try {
            return gradeRepository.findAll().stream().map(GradeMapper::toDTO).collect(Collectors.toList());
        } catch (GradesNotFoundException e) {
            throw new GradesNotFoundException("Could not retrieve grades: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while retrieving grades: " + e.getMessage());
        }
    }

    @Override
    public GradeResponse getGradeById(UUID id) {
        try {
            Grade grade = gradeRepository.findById(id)
                    .orElseThrow(() -> new GradeNotCreatedException("Grade not found with id: " + id));
    
            return GradeMapper.toDTO(grade);
        } catch (GradeCouldNotBeDeletedException e) {
            throw new GradeCouldNotBeDeletedException("Could not retrieve grade: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while retrieving the grade: " + e.getMessage());
        }
    }

    @Override
    @ComplianceAudit(entityType = AuditEntityType.GRADE_ASSIGNED, scope = "Verify Grade update to a particular Student")
    public GradeResponse updateGrade(UUID id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradesNotFoundException("Grade not found with id: " + id));

        Exam exam = examRepository.findById(request.examId())
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + request.examId()));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + request.studentId()));

        try {
            grade.setExam(exam);
            grade.setStudent(student);
            grade.setScore(request.score());
            grade.setGrade(request.grade());
            grade.setStatus(request.status());

            Grade updatedGrade = gradeRepository.save(grade);
            return GradeMapper.toDTO(updatedGrade);
        } catch (GradeNotUpdatedException e) {
            throw new GradeNotUpdatedException("Could not update grade: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while updating the grade: " + e.getMessage());
        }
    }

    @Override
    public void deleteGrade(UUID id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradesNotFoundException("Grade not found with id: " + id));

        try {
            gradeRepository.delete(grade);
        } catch (GradeCouldNotBeDeletedException e) {
            throw new GradeCouldNotBeDeletedException("Cannot delete the grade: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while deleting the grade: " + e.getMessage());
        }
    }

    @Override
    public List<GradeResponse> getGradesByStudent(UUID studentId) {
        try {
            return gradeRepository.findByStudentId(studentId).stream().map(GradeMapper::toDTO).collect(Collectors.toList());
        } catch (GradesNotFoundException e) {
            throw new GradesNotFoundException("Could not retrieve grades for student with id: " + studentId);
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while retrieving grades for the student: " + e.getMessage());
        }
    }

    @Override
    public List<GradeResponse> getGradesByExam(UUID examId) {
        try {
            return gradeRepository.findByExamId(examId).stream().map(GradeMapper::toDTO).collect(Collectors.toList());
        } catch (GradesNotFoundException e) {
            throw new GradesNotFoundException("Could not retrieve grades for exam with id: " + examId);
        } catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while retrieving grades for the exam: " + e.getMessage());
        }
    }
}