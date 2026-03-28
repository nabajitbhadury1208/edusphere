package com.cts.edusphere.repositories.compliance;

import com.cts.edusphere.modules.compliance_record.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link ComplianceRecord} entities.
 *
 * <p>Provides CRUD operations inherited from {@link JpaRepository} and additional
 * query methods for retrieving compliance records by officer and entity association.</p>
 */
@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, UUID> {

    /**
     * Retrieves all compliance records recorded by a specific compliance officer.
     *
     * <p>Uses nested property navigation on {@code recordedBy.id} to match
     * the officer's user ID.</p>
     *
     * @param userId the {@link UUID} of the compliance officer whose records are to be fetched
     * @return a {@link List} of {@link ComplianceRecord} instances associated with the given officer;
     *         an empty list if none are found
     */
    // Nested property navigation: recordedBy.id
    List<ComplianceRecord> findByComplianceOfficer_Id(UUID userId);

    /**
     * Retrieves all compliance records associated with a specific module instance (entity).
     *
     * @param entityId the {@link UUID} of the entity (module instance) to filter records by
     * @return a {@link List} of {@link ComplianceRecord} instances linked to the given entity ID;
     *         an empty list if none are found
     */
    // Search records for a specific module instance
    List<ComplianceRecord> findByEntityId(UUID entityId);
}
