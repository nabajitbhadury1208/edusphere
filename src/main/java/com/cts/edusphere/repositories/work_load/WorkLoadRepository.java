package com.cts.edusphere.repositories.work_load;


import com.cts.edusphere.modules.work_load.WorkLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkLoadRepository extends JpaRepository<WorkLoad, UUID> {
    List<WorkLoad> findByFacultyId(UUID facultyId);
}

