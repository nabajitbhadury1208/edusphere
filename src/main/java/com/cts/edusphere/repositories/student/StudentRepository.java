package com.cts.edusphere.repositories.student;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Student} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom lookup methods for retrieving students by email, phone, and enrollment status.</p>
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    /**
     * Retrieves a student by their email address.
     *
     * <p>Typically used for uniqueness validation and authentication resolution.</p>
     *
     * @param email the email address of the student to look up
     * @return an {@link Optional} containing the matching {@link Student},
     *         or {@link Optional#empty()} if no student has the given email
     */
    Optional<Student> findByEmail(String email);

    /**
     * Retrieves a student by their phone number.
     *
     * <p>Typically used for uniqueness checks during registration or profile updates.</p>
     *
     * @param phone the phone number of the student to look up
     * @return an {@link Optional} containing the matching {@link Student},
     *         or {@link Optional#empty()} if no student has the given phone number
     */
    Optional<Student> findByPhone(String phone);

    /**
     * Retrieves all students with the specified enrollment/account status.
     *
     * @param status the {@link Status} enum value (e.g., {@code ACTIVE}, {@code INACTIVE})
     *               used to filter students
     * @return a {@link List} of all {@link Student} instances with the given status;
     *         an empty list if no students match
     */
    List<Student> findAllByStatus(Status status);
}
