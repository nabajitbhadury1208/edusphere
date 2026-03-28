package com.cts.edusphere.repositories.exam;

import com.cts.edusphere.modules.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Exam} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations, along with
 * a custom query method for retrieving exams associated with a specific course.</p>
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    /**
     * Retrieves all exams associated with the specified course.
     *
     * @param courseId the {@link UUID} of the course whose exams are to be fetched
     * @return a {@link List} of {@link Exam} instances belonging to the given course;
     *         an empty list if no exams are found for that course
     */
    List<Exam> findByCourseId(UUID courseId);
}
