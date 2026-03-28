package com.cts.edusphere.services.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;
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

/**
 * Service implementation for managing exam-related operations.
 *
 * <p>Provides the core business logic for creating, retrieving, updating, and deleting
 * {@link Exam} entities, as well as filtering exams by the associated {@link Course}.
 * Every public method maps incoming request DTOs to domain objects via {@link ExamMapper},
 * delegates persistence to {@link ExamRepository}, and maps the results back to response DTOs.
 *
 * <p>Domain-specific exceptions (e.g. {@link ExamNotFoundException},
 * {@link ExamNotCreatedException}) are caught and re-thrown to provide consistent,
 * caller-friendly error messages; all other unexpected failures are wrapped in
 * {@link InternalServerErrorException}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;

    /**
     * Creates a new exam and associates it with an existing course.
     *
     * <p>The course identified by {@link ExamRequest#courseId()} is resolved first. If the
     * course does not exist the operation is aborted immediately. On success the persisted
     * exam is returned as a response DTO.
     *
     * @param request the DTO containing the exam details, including the mandatory
     *                {@code courseId} of the owning course
     * @return an {@link ExamResponse} representing the newly created exam
     * @throws CourseNotFoundException      if no course exists with the given {@code courseId}
     * @throws ExamNotCreatedException      if a domain-level error prevents exam creation
     * @throws InternalServerErrorException if an unexpected error occurs during creation
     */
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

    /**
     * Retrieves all exams stored in the system.
     *
     * @return a {@link List} of {@link ExamResponse} objects representing every exam;
     *         never {@code null}, but may be empty
     * @throws ExamsNotFoundException       if a domain-level error signals that exams cannot
     *                                      be retrieved
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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

    /**
     * Retrieves a single exam by its unique identifier.
     *
     * @param id the {@link UUID} of the exam to retrieve
     * @return an {@link ExamResponse} representing the found exam
     * @throws ExamNotFoundException        if no exam exists with the given {@code id}
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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

    /**
     * Replaces all mutable fields of an existing exam with the values from the request DTO.
     *
     * <p>Both the exam and the course referenced in {@code request} must exist. All four
     * mutable fields — course, type, date, and status — are unconditionally overwritten.
     *
     * @param id      the {@link UUID} of the exam to update
     * @param request the DTO containing the replacement field values, including the
     *                {@code courseId} of the (possibly new) owning course
     * @return an {@link ExamResponse} representing the updated exam
     * @throws ExamNotFoundException            if no exam exists with the given {@code id}
     * @throws ResourceNotFoundException        if no course exists with the given {@code courseId}
     * @throws ExamCouldNotBeUpdatedException   if a domain-level error prevents the update
     * @throws InternalServerErrorException     if an unexpected error occurs during the update
     */
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

    /**
     * Updates only the {@link Status} of an existing exam, leaving all other fields unchanged.
     *
     * @param id     the {@link UUID} of the exam whose status is to be updated
     * @param status the new {@link Status} value to apply
     * @return an {@link ExamResponse} representing the exam after the status change
     * @throws ExamNotFoundException          if no exam exists with the given {@code id}
     * @throws ExamCouldNotBeUpdatedException if a domain-level error prevents the status update
     * @throws InternalServerErrorException   if an unexpected error occurs during the update
     */
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

    /**
     * Deletes the exam identified by the given {@code id}.
     *
     * <p>The exam is first fetched to confirm it exists before the delete is issued,
     * ensuring a meaningful error is returned rather than a silent no-op.
     *
     * @param id the {@link UUID} of the exam to delete
     * @throws ExamNotFoundException            if no exam exists with the given {@code id}
     * @throws ExamCouldNotBeDeletedException   if a domain-level error prevents deletion
     * @throws InternalServerErrorException     if an unexpected error occurs during deletion
     */
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

    /**
     * Retrieves all exams associated with a specific course.
     *
     * @param courseId the {@link UUID} of the course whose exams are to be retrieved
     * @return a {@link List} of {@link ExamResponse} objects for the given course;
     *         never {@code null}, but may be empty if no exams are linked to the course
     * @throws ExamsNotFoundException       if a domain-level error signals that the exams
     *                                      for the course cannot be retrieved
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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
