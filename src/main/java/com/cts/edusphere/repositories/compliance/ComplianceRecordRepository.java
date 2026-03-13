package com.cts.edusphere.repositories.compliance;

import com.cts.edusphere.modules.compliance_record.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, UUID> {
    // Nested property navigation: recordedBy.id
    List<ComplianceRecord> findByComplianceOfficer_Id(UUID userId);

    // Search records for a specific module instance
    List<ComplianceRecord> findByEntityId(UUID entityId);
}