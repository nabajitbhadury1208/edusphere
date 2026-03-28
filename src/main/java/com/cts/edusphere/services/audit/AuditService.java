package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditEntityType;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for audit management within EduSphere.
 *
 * <p>Provides methods for reviewing, retrieving, and deleting audit records,
 * as well as filtering audits by the type of entity they relate to. Audit records
 * capture significant changes or events for governance and compliance purposes.</p>
 */
public interface AuditService {

    /**
     * Reviews and updates an existing audit record with the reviewer's findings or annotations.
     *
     * @param auditId the {@link UUID} of the audit record to review
     * @param dto     the {@link AuditRequestDTO} containing the reviewer's input (e.g., comments, outcome); must not be {@code null}
     * @return an {@link AuditResponseDTO} reflecting the updated audit record after review
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no audit record exists with the given ID
     */
    AuditResponseDTO reviewAudit(UUID auditId, AuditRequestDTO dto);

    /**
     * Retrieves all audit records in the system.
     *
     * @return a {@link List} of {@link AuditResponseDTO} objects representing all audit records;
     *         never {@code null}, may be empty
     */
    List<AuditResponseDTO> getAllAudits();

    /**
     * Retrieves a single audit record by its unique identifier.
     *
     * @param id the {@link UUID} of the audit record to retrieve
     * @return an {@link AuditResponseDTO} representing the found audit record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no audit record exists with the given ID
     */
    AuditResponseDTO getAuditById(UUID id);

    /**
     * Deletes the audit record identified by the given ID.
     *
     * @param id the {@link UUID} of the audit record to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no audit record exists with the given ID
     */
    void deleteAudit(UUID id);

    /**
     * Retrieves all audit records associated with a specific entity type.
     *
     * @param entityType the {@link AuditEntityType} to filter by (e.g., COURSE, FACULTY, STUDENT)
     * @return a {@link List} of {@link AuditResponseDTO} objects for the given entity type;
     *         never {@code null}, may be empty if no audits exist for that type
     */
    List<AuditResponseDTO> getAuditsByEntityType(AuditEntityType entityType);
}
