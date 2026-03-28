package com.cts.edusphere.services.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import java.util.*;

/**
 * Service interface defining the contract for grade management operations within EduSphere.
 *
 * <p>Provides methods for recording, retrieving, updating, and deleting student grades,
 * as well as querying grades by student or by exam. All operations use Data Transfer
 * Objects (DTOs) to decouple the service layer from the underlying persistence model.</p>
 */
public interface GradeService {

    /**
     * Records a new grade from the provided request data.
     *
     * @param request the {@link GradeRequest} containing the grade details
     *                (student, exam, score, etc.); must not be {@code null}
     * @return a {@link GradeResponse} representing the newly recorded grade
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException  if the referenced student or exam does not exist
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a grade for the same student and exam already exists
     */
    GradeResponse createGrade(GradeRequest request);

    /**
     * Retrieves all grade records in the system.
     *
     * @return a {@link List} of {@link GradeResponse} objects representing all grades;
     *         never {@code null}, may be empty
     */
    List<GradeResponse> getAllGrades();

    /**
     * Retrieves a single grade record by its unique identifier.
     *
     * @param id the {@link UUID} of the grade to retrieve
     * @return a {@link GradeResponse} representing the found grade record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no grade exists with the given ID
     */
    GradeResponse getGradeById(UUID id);

    /**
     * Updates an existing grade record with new values.
     *
     * @param id      the {@link UUID} of the grade to update
     * @param request the {@link GradeRequest} containing the updated field values; must not be {@code null}
     * @return a {@link GradeResponse} representing the updated grade record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no grade exists with the given ID
     */
    GradeResponse updateGrade(UUID id, GradeRequest request);

    /**
     * Deletes the grade record identified by the given ID.
     *
     * @param id the {@link UUID} of the grade to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no grade exists with the given ID
     */
    void deleteGrade(UUID id);

    /**
     * Retrieves all grade records associated with a specific student.
     *
     * @param studentId the {@link UUID} of the student whose grades are to be retrieved
     * @return a {@link List} of {@link GradeResponse} objects for the given student;
     *         never {@code null}, may be empty if the student has no recorded grades
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    List<GradeResponse> getGradesByStudent(UUID studentId);

    /**
     * Retrieves all grade records associated with a specific exam.
     *
     * @param examId the {@link UUID} of the exam whose grades are to be retrieved
     * @return a {@link List} of {@link GradeResponse} objects for the given exam;
     *         never {@code null}, may be empty if no grades have been recorded for the exam
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no exam exists with the given ID
     */
    List<GradeResponse> getGradesByExam(UUID examId);

}
