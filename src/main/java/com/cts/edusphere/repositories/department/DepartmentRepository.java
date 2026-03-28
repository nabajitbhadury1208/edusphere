package com.cts.edusphere.repositories.department;

import com.cts.edusphere.modules.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Department} entities.
 *
 * <p>Provides standard CRUD operations via {@link JpaRepository}, as well as custom
 * query methods for looking up departments by code, name, active status, and
 * department head association.</p>
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    /**
     * Retrieves a department by its unique department code.
     *
     * @param departmentCode the unique alphanumeric code identifying the department
     * @return an {@link Optional} containing the matching {@link Department},
     *         or {@link Optional#empty()} if no department has the given code
     */
    Optional<Department> findByDepartmentCode(String departmentCode);

    /**
     * Retrieves a department by its unique department name.
     *
     * @param departmentName the name of the department to look up
     * @return an {@link Optional} containing the matching {@link Department},
     *         or {@link Optional#empty()} if no department has the given name
     */
    Optional<Department> findByDepartmentName(String departmentName);

    /**
     * Retrieves all departments whose status is {@code ACTIVE}.
     *
     * <p>Uses a JPQL query to filter departments by their {@code status} field.</p>
     *
     * @return a {@link List} of {@link Department} instances with status {@code ACTIVE};
     *         an empty list if no active departments exist
     * @Query("SELECT d FROM Department d WHERE d.status = 'ACTIVE'")
     */
    @Query("SELECT d FROM Department d WHERE d.status = 'ACTIVE'")
    List<Department> findAllActiveDepartments();

    /**
     * Retrieves the department whose department head matches the given user ID.
     *
     * <p>Uses nested property navigation on {@code departmentHead.id}.</p>
     *
     * @param headId the {@link UUID} of the user serving as department head
     * @return an {@link Optional} containing the {@link Department} led by the specified user,
     *         or {@link Optional#empty()} if no department has that head
     */
    Optional<Department> findByDepartmentHead_Id(UUID headId);
}
