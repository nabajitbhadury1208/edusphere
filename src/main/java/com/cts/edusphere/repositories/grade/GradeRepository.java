package com.cts.edusphere.repositories.grade;

import com.cts.edusphere.modules.grade.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Grade} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom query methods for retrieving grade records filtered by student or exam.</p>
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {

    /**
     * Retrieves all grade records associated with a specific student.
     *
     * @param studentId the {@link UUID} of the student whose grades are to be fetched
     * @return a {@link List} of {@link Grade} instances belonging to the given student;
     *         an empty list if no grades are found for that student
     */
    List<Grade> findByStudentId(UUID studentId);

    /**
     * Retrieves all grade records associated with a specific exam.
     *
     * @param examId the {@link UUID} of the exam whose grade records are to be fetched
     * @return a {@link List} of {@link Grade} instances recorded for the given exam;
     *         an empty list if no grades are found for that exam
     */
    List<Grade> findByExamId(UUID examId);
}
