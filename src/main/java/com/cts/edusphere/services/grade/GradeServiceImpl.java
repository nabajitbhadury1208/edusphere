package com.cts.edusphere.services.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.enums.GradeStatus;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.GradeMapper;
import com.cts.edusphere.modules.Exam;
import com.cts.edusphere.modules.Grade;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.repositories.ExamRepository;
import com.cts.edusphere.repositories.GradeRepository;
import com.cts.edusphere.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
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
    public GradeResponse createGrade(GradeRequest request) {
        Exam exam = examRepository.findById(request.examId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + request.examId()));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.studentId()));

        try {
            Grade grade = GradeMapper.toEntity(request, exam, student);
            Grade savedGrade = gradeRepository.save(grade);
            return GradeMapper.toDTO(savedGrade);
        } catch (Exception e) {
            throw new RuntimeException("Could not create grade: " + e.getMessage());
        }
    }

    @Override
    public List<GradeResponse> getAllGrades() {
        return gradeRepository.findAll()
                .stream()
                .map(GradeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GradeResponse getGradeById(UUID id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        return GradeMapper.toDTO(grade);
    }

    @Override
    public GradeResponse updateGrade(UUID id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        Exam exam = examRepository.findById(request.examId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + request.examId()));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.studentId()));

        try {
            grade.setExam(exam);
            grade.setStudent(student);
            grade.setScore(request.score());
            grade.setGrade(request.grade());
            grade.setStatus(request.status());

            Grade updatedGrade = gradeRepository.save(grade);
            return GradeMapper.toDTO(updatedGrade);
        } catch (Exception e) {
            throw new RuntimeException("Could not update grade: " + e.getMessage());
        }
    }

    @Override
    public void deleteGrade(UUID id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        try {
            gradeRepository.delete(grade);
        } catch (Exception e) {
            throw new CannotDeleteException("Cannot delete the grade: " + e.getMessage());
        }
    }

    @Override
    public List<GradeResponse> getGradesByStudent(UUID studentId) {
        return gradeRepository.findByStudentId(studentId)
                .stream()
                .map(GradeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeResponse> getGradesByExam(UUID examId) {
        return gradeRepository.findByExamId(examId)
                .stream()
                .map(GradeMapper::toDTO)
                .collect(Collectors.toList());
    }
}