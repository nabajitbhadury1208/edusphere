package com.cts.edusphere.repositories.thesis;

import com.cts.edusphere.modules.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Thesis} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom query methods for retrieving thesis records filtered by student or
 * supervising faculty member.</p>
 */
@Repository
public interface ThesisRepository extends JpaRepository<Thesis, UUID> {

    /**
     * Retrieves all thesis records associated with a specific student.
     *
     * @param studentId the {@link UUID} of the student whose thesis records are to be fetched
     * @return a {@link List} of {@link Thesis} instances belonging to the given student;
     *         an empty list if no thesis records are found for that student
     */
    List<Thesis> findByStudentId(UUID studentId);

    /**
     * Retrieves all thesis records supervised by a specific faculty member.
     *
     * @param facultyId the {@link UUID} of the faculty member (supervisor) whose supervised
     *                  theses are to be fetched
     * @return a {@link List} of {@link Thesis} instances supervised by the given faculty member;
     *         an empty list if no thesis records are found for that supervisor
     */
    List<Thesis> findBySupervisorId(UUID facultyId);
}
