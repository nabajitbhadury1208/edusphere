package com.cts.edusphere.repositories.work_load;


import com.cts.edusphere.modules.work_load.WorkLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link WorkLoad} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * a custom query method for retrieving workload records assigned to a specific
 * faculty member.</p>
 */
@Repository
public interface WorkLoadRepository extends JpaRepository<WorkLoad, UUID> {

    /**
     * Retrieves all workload records assigned to a specific faculty member.
     *
     * @param facultyId the {@link UUID} of the faculty member whose workload records
     *                  are to be fetched
     * @return a {@link List} of {@link WorkLoad} instances assigned to the given faculty member;
     *         an empty list if no workload records are found for that faculty member
     */
    List<WorkLoad> findByFacultyId(UUID facultyId);
}
