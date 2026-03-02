package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.WorkLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkLoadRepository extends JpaRepository<WorkLoad, UUID> {
}

