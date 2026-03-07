package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, UUID> {
    // Nested property navigation: recordedBy.id
    List<ComplianceRecord> findByRecordedBy_Id(UUID userId);

    // Search records for a specific module instance
    List<ComplianceRecord> findByEntityId(UUID entityId);
}