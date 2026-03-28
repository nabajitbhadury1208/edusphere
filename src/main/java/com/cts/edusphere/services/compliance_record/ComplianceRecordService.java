package com.cts.edusphere.services.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for compliance record management within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting compliance records,
 * as well as querying records by the entity they govern or the user they are linked to.
 * Compliance records document adherence to institutional policies, regulatory requirements,
 * or accreditation standards.</p>
 */
public interface ComplianceRecordService {

    /**
     * Creates a new compliance record from the provided request data.
     *
     * @param request the {@link ComplianceRecordRequest} containing the compliance details
     *                (policy, status, entity reference, etc.); must not be {@code null}
     * @return a {@link ComplianceRecordResponse} representing the newly created compliance record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if a referenced entity or user does not exist
     */
    ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request);

    /**
     * Retrieves all compliance records in the system.
     *
     * @return a {@link List} of {@link ComplianceRecordResponse} objects representing all compliance records;
     *         never {@code null}, may be empty
     */
    List<ComplianceRecordResponse> getAllComplianceRecords();

    /**
     * Retrieves a single compliance record by its unique identifier.
     *
     * @param id the {@link UUID} of the compliance record to retrieve
     * @return a {@link ComplianceRecordResponse} representing the found compliance record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no compliance record exists with the given ID
     */
    ComplianceRecordResponse getComplianceRecordById(UUID id);

    /**
     * Retrieves all compliance records associated with a specific entity (e.g., a department or course).
     *
     * @param entityId the {@link UUID} of the entity whose compliance records are to be retrieved
     * @return a {@link List} of {@link ComplianceRecordResponse} objects for the given entity;
     *         never {@code null}, may be empty if the entity has no compliance records
     */
    List<ComplianceRecordResponse> getComplianceRecordsByEntityId(UUID entityId);

    /**
     * Retrieves all compliance records linked to a specific user.
     *
     * @param userId the {@link UUID} of the user whose compliance records are to be retrieved
     * @return a {@link List} of {@link ComplianceRecordResponse} objects for the given user;
     *         never {@code null}, may be empty if the user has no compliance records
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
     */
    List<ComplianceRecordResponse> getComplianceRecordsByUserId(UUID userId);

    /**
     * Updates an existing compliance record with new values.
     *
     * @param id      the {@link UUID} of the compliance record to update
     * @param request the {@link ComplianceRecordRequest} containing the updated field values; must not be {@code null}
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no compliance record exists with the given ID
     */
    void updateComplianceRecord(UUID id, ComplianceRecordRequest request);

    /**
     * Deletes the compliance record identified by the given ID.
     *
     * @param id the {@link UUID} of the compliance record to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no compliance record exists with the given ID
     */
    void deleteComplianceRecordById(UUID id);
}
