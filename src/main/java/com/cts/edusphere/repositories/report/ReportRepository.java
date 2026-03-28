package com.cts.edusphere.repositories.report;

import com.cts.edusphere.modules.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Report} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * a custom query method for retrieving reports scoped to a specific department.</p>
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    /**
     * Retrieves all reports associated with a specific department.
     *
     * @param departmentId the {@link UUID} of the department whose reports are to be fetched
     * @return a {@link List} of {@link Report} instances belonging to the given department;
     *         an empty list if no reports are found for that department
     */
    List<Report> findByDepartmentId(UUID departmentId);
}
