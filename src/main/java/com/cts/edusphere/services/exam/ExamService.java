package com.cts.edusphere.services.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for exam management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, deleting, and filtering exams.
 * It also supports updating the lifecycle status of an exam (e.g., SCHEDULED, ONGOING,
 * COMPLETED) and querying exams associated with a particular course.</p>
 */
public interface ExamService {

    /**
     * Creates a new exam from the provided request data.
     *
     * @param request the {@link ExamRequest} containing the exam details; must not be {@code null}
     * @return an {@link ExamResponse} representing the newly created exam
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if the referenced course does not exist
     */
    ExamResponse createExam(ExamRequest request);

    /**
     * Retrieves all exams in the system.
     *
     * @return a {@link List} of {@link ExamResponse} objects representing all exams;
     *         never {@code null}, may be empty
     */
    List<ExamResponse> getAllExams();

    /**
     * Retrieves a single exam by its unique identifier.
     *
     * @param id the {@link UUID} of the exam to retrieve
     * @return an {@link ExamResponse} representing the found exam
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no exam exists with the given ID
     */
    ExamResponse getExamById(UUID id);

    /**
     * Updates the details of an existing exam.
     *
     * @param id      the {@link UUID} of the exam to update
     * @param request the {@link ExamRequest} containing the updated field values; must not be {@code null}
     * @return an {@link ExamResponse} representing the updated exam
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no exam exists with the given ID
     */
    ExamResponse updateExam(UUID id, ExamRequest request);

    /**
     * Updates only the status of an existing exam without modifying other fields.
     *
     * @param id     the {@link UUID} of the exam whose status is to be updated
     * @param status the new {@link Status} to apply to the exam
     * @return an {@link ExamResponse} representing the exam with the updated status
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no exam exists with the given ID
     */
    ExamResponse updateExamStatus(UUID id, Status status);

    /**
     * Deletes the exam identified by the given ID.
     *
     * @param id the {@link UUID} of the exam to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no exam exists with the given ID
     */
    void deleteExam(UUID id);

    /**
     * Retrieves all exams associated with a specific course.
     *
     * @param courseId the {@link UUID} of the course whose exams are to be retrieved
     * @return a {@link List} of {@link ExamResponse} objects for the given course;
     *         never {@code null}, may be empty if the course has no exams
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no course exists with the given ID
     */
    List<ExamResponse> getExamsByCourse(UUID courseId);


}
