package com.cts.edusphere.repositories.faculty;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Faculty} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom lookup methods for retrieving faculty members by email, phone, department,
 * and employment status.</p>
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, UUID> {

    /**
     * Retrieves a faculty member by their email address.
     *
     * <p>Typically used for uniqueness checks and login resolution.</p>
     *
     * @param email the email address of the faculty member to look up
     * @return an {@link Optional} containing the matching {@link Faculty},
     *         or {@link Optional#empty()} if no faculty member has the given email
     */
    Optional<Faculty> findByEmail(String email);

    /**
     * Retrieves a faculty member by their phone number.
     *
     * <p>Typically used for uniqueness checks during registration or profile updates.</p>
     *
     * @param phone the phone number of the faculty member to look up
     * @return an {@link Optional} containing the matching {@link Faculty},
     *         or {@link Optional#empty()} if no faculty member has the given phone number
     */
    Optional<Faculty> findByPhone(String phone);

    /**
     * Retrieves all faculty members belonging to a specific department.
     *
     * @param departmentId the {@link UUID} of the department whose faculty are to be fetched
     * @return a {@link List} of {@link Faculty} instances belonging to the specified department;
     *         an empty list if no faculty members are found for that department
     */
    List<Faculty> findByDepartmentId(UUID departmentId);

    /**
     * Retrieves all faculty members in a specific department who have a given employment status.
     *
     * @param departmentId the {@link UUID} of the department to filter by
     * @param status       the {@link Status} enum value (e.g., {@code ACTIVE}, {@code INACTIVE})
     *                     to filter faculty by
     * @return a {@link List} of {@link Faculty} instances matching both the department and status;
     *         an empty list if no matches are found
     */
    List<Faculty> findByDepartmentIdAndStatus(UUID departmentId, Status status);

    /**
     * Retrieves all faculty members with the specified employment status.
     *
     * @param status the {@link Status} enum value (e.g., {@code ACTIVE}, {@code INACTIVE})
     *               used to filter faculty members
     * @return a {@link List} of all {@link Faculty} instances with the given status;
     *         an empty list if no faculty members match
     */
    List<Faculty> findAllByStatus(Status status);
}
