package com.cts.edusphere.services.grade;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.CannotDeleteException;
import com.cts.edusphere.exceptions.genericexceptions.ExamNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.GradeNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.GradeCouldNotBeDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.GradeNotUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.GradesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.StudentNotFoundException;
import com.cts.edusphere.mappers.grade.GradeMapper;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.modules.grade.Grade;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.exam.ExamRepository;
import com.cts.edusphere.repositories.grade.GradeRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing student grade business operations.
 *
 * <p>Provides full CRUD functionality for {@link Grade} entities, along with
 * convenience queries that filter grades by student or exam. Each write operation
 * validates that the referenced {@link Exam} and {@link Student} exist before
 * persisting changes. Compliance-sensitive operations are audited via the
 * {@code @ComplianceAudit} AOP aspect.</p>
 *
 * <p>Dependencies are injected via constructor (Lombok {@code @RequiredArgsConstructor}).
 * Static mapper methods from {@link GradeMapper} are used to convert between
 * entities and DTOs.</p>
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    /**
     * Creates a new grade record and persists it to the database.
     *
     * <p>Resolves the {@link Exam} and {@link Student} referenced in the request,
     * maps the request to a {@link Grade} entity via {@link GradeMapper#toEntity},
     * saves the record, and returns the persisted data as a DTO. A compliance audit
     * event of type {@link AuditEntityType#GRADE_ASSIGNED} is recorded by the AOP
     * aspect.</p>
     *
     * @param request the {@link GradeRequest} containing examId, studentId, score,
     *                grade letter, and status
     * @return a {@link GradeResponse} representing the newly created grade record
     * @throws ExamNotFoundException        if no exam exists with the given {@code examId}
     * @throws StudentNotFoundException     if no student exists with the given {@code studentId}
     * @throws GradeNotCreatedException     if a domain-level creation constraint is violated
     * @throws InternalServerErrorException if any unexpected error occurs during creation
     */
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

    /**
     * Retrieves all grade records stored in the system.
     *
     * <p>Streams all {@link Grade} entities from the repository and maps each one
     * to a {@link GradeResponse} DTO.</p>
     *
     * @return a {@link List} of {@link GradeResponse} objects; may be empty if no
     *         grade records exist
     * @throws GradesNotFoundException      if a domain-level error prevents listing grades
     * @throws InternalServerErrorException if any unexpected error occurs during retrieval
     */
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

    /**
     * Retrieves a single grade record by its unique identifier.
     *
     * <p>Looks up the {@link Grade} entity by {@code id} and maps it to a
     * {@link GradeResponse} DTO before returning.</p>
     *
     * @param id the {@link UUID} of the grade record to retrieve
     * @return a {@link GradeResponse} containing the grade details
     * @throws GradeNotCreatedException         if no grade record exists with the given {@code id}
     * @throws GradeCouldNotBeDeletedException  if a domain-level retrieval error occurs
     * @throws InternalServerErrorException     if any unexpected error occurs during retrieval
     */
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

    /**
     * Updates all fields of an existing grade record.
     *
     * <p>Resolves the grade, the referenced {@link Exam}, and the referenced
     * {@link Student} before applying all fields from the request. Unlike a partial
     * update, every field is overwritten unconditionally. A compliance audit event
     * of type {@link AuditEntityType#GRADE_ASSIGNED} is recorded by the AOP aspect.</p>
     *
     * @param id      the {@link UUID} of the grade record to update
     * @param request the {@link GradeRequest} containing the new examId, studentId,
     *                score, grade letter, and status
     * @return a {@link GradeResponse} reflecting the grade record's state after the update
     * @throws GradesNotFoundException      if no grade record exists with the given {@code id}
     * @throws ExamNotFoundException        if no exam exists with the given {@code examId}
     * @throws StudentNotFoundException     if no student exists with the given {@code studentId}
     * @throws GradeNotUpdatedException     if a domain-level update constraint is violated
     * @throws InternalServerErrorException if any unexpected error occurs during the update
     */
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

    /**
     * Deletes a grade record from the system by its unique identifier.
     *
     * <p>Locates the {@link Grade} entity and removes it from the repository.
     * The lookup happens outside the inner try-catch so that a missing record
     * propagates immediately as a {@link GradesNotFoundException}.</p>
     *
     * @param id the {@link UUID} of the grade record to delete
     * @throws GradesNotFoundException          if no grade record exists with the given {@code id}
     * @throws GradeCouldNotBeDeletedException  if a domain-level deletion constraint is violated
     * @throws InternalServerErrorException     if any unexpected error occurs during deletion
     */
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

    /**
     * Retrieves all grade records associated with a specific student.
     *
     * <p>Delegates to {@code GradeRepository#findByStudentId} and maps each
     * result to a {@link GradeResponse} DTO.</p>
     *
     * @param studentId the {@link UUID} of the student whose grades are requested
     * @return a {@link List} of {@link GradeResponse} objects for the given student;
     *         may be empty if the student has no grades recorded
     * @throws GradesNotFoundException      if a domain-level error prevents listing grades
     * @throws InternalServerErrorException if any unexpected error occurs during retrieval
     */
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

    /**
     * Retrieves all grade records associated with a specific exam.
     *
     * <p>Delegates to {@code GradeRepository#findByExamId} and maps each result
     * to a {@link GradeResponse} DTO.</p>
     *
     * @param examId the {@link UUID} of the exam whose grade records are requested
     * @return a {@link List} of {@link GradeResponse} objects for the given exam;
     *         may be empty if no grades have been recorded for the exam
     * @throws GradesNotFoundException      if a domain-level error prevents listing grades
     * @throws InternalServerErrorException if any unexpected error occurs during retrieval
     */
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
