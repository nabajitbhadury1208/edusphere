package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.AuditNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.AuditsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.AuditNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.FailedToReviewAuditException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.OfficerNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.audit.AuditMapper;
import com.cts.edusphere.modules.audit.Audit;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.audit.AuditRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import com.cts.edusphere.services.audit_log.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing compliance audit records.
 * Handles business logic for reviewing, retrieving, filtering, and deleting audit entries.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final UserRepository userRepository;

    /**
     * Reviews an existing audit record by assigning a compliance officer, recording findings,
     * setting the audit date to today, and optionally updating the audit status.
     *
     * @param auditId the UUID of the audit record to review
     * @param dto     the request containing officerId, findings, and optional new status
     * @return the updated AuditResponseDTO after the review
     * @throws AuditNotFoundException       if no audit with the given ID exists
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    @Transactional
    public AuditResponseDTO reviewAudit(UUID auditId,AuditRequestDTO dto) {
        try {
            Audit audit = auditRepository.findById(auditId)
                    .orElseThrow(() -> new AuditNotFoundException("Audit record not found"));
            User complianceOfficer = findOfficerById(dto.officerId());

            audit.setComplianceOfficer(complianceOfficer);
            audit.setFindings(dto.findings());
            audit.setAuditDate(LocalDate.now());
            if(dto.status() != null){
                audit.setStatus(dto.status());
            }

            Audit savedAudit = auditRepository.save(audit);
            log.info("Audit record reviewed successfully with ID: {}", savedAudit.getId());
            return auditMapper.toResponseDTO(savedAudit);
        } catch (FailedToReviewAuditException e) {
            log.error("Error occurred while reviewing audit record: {}", e.getMessage());
            throw new FailedToReviewAuditException("Failed to Review Audit Message");
        } catch (Exception e) {
            log.error("Unexpected error occurred while reviewing audit record: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to review audit record");
        }
    }

    /**
     * Retrieves all audit records from the database and maps them to response DTOs.
     * Uses a read-only transaction for performance optimization.
     *
     * @return a list of all AuditResponseDTO objects
     * @throws AuditsNotFoundException      if no audit records are available
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAllAudits() {
        try {
            return auditRepository.findAll().stream().map(auditMapper::toResponseDTO).collect(Collectors.toList());
        } catch (AuditsNotFoundException e) {
            log.error("Error occurred while fetching all audit records: {}", e.getMessage());
            throw new AuditsNotFoundException("Failed to retrieve audit records");
        } catch(Exception e) {
            log.error("Unexpected error occurred while fetching all audit records: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit records");
        }
    }

    /**
     * Retrieves a single audit record by its unique identifier.
     *
     * @param id the UUID of the audit record to retrieve
     * @return the matching AuditResponseDTO
     * @throws AuditNotFoundException       if no audit with the given ID exists
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    @Transactional(readOnly = true)
    public AuditResponseDTO getAuditById(UUID id) {
        try {
            return auditRepository.findById(id).map(auditMapper::toResponseDTO)
                    .orElseThrow(() -> new AuditNotFoundException("Audit record not found with id: " + id));
            // } catch (AuditNotFoundException e) {
            // log.error("Error occurred while fetching audit record with ID {}: {}", id,
            // e.getMessage());
            // throw new AuditNotFoundException("Audit with id: " + id + "not found");
        } catch (InternalServerErrorException e) {
            log.error("Error occurred while fetching audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit record");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit record");
        }
    }

    /**
     * Permanently deletes an audit record by its unique identifier.
     * Verifies existence before deletion to throw a descriptive exception.
     *
     * @param id the UUID of the audit record to delete
     * @throws AuditNotFoundException       if no audit with the given ID exists
     * @throws AuditNotDeletedException     if a domain-specific deletion failure occurs
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Transactional
    public void deleteAudit(UUID id) {
        try {
            if (!auditRepository.existsById(id)) {
                throw new AuditNotFoundException("Audit record not found with id: " + id);
            }
            auditRepository.deleteById(id);
            log.info("Audit record with ID {} deleted successfully", id);
            // } catch (ResourceNotFoundException e) {
            // throw e;
        } catch (AuditNotDeletedException e) {
            log.error("Error occurred while deleting audit record with ID {}: {}", id, e.getMessage());
            throw new AuditNotDeletedException("Failed to delete audit record");
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete audit record");
        }
    }

    /**
     * Retrieves all audit records matching a specific entity type.
     *
     * @param entityType the AuditEntityType enum value to filter by
     * @return a list of AuditResponseDTO objects matching the entity type
     * @throws AuditNotFoundException       if no matching audits are found
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditResponseDTO> getAuditsByEntityType(AuditEntityType entityType) {
        try {
            return auditRepository.findByEntityType(entityType).stream().map(auditMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditNotFoundException e) {
            log.error("Error fetching audits by entity type {}: {}", entityType, e.getMessage());
            throw new AuditNotFoundException("Failed to retrieve audits by entity type");
        } catch (Exception e) {
            log.error("Unexpected error fetching audits by entity type {}: {}", entityType, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audits by entity type");
        }
    }

    /**
     * Helper method that validates and retrieves a compliance officer by their user ID.
     * Ensures the user has the COMPLIANCE_OFFICER or ADMIN role before returning.
     *
     * @param officerId the UUID of the user to validate as a compliance officer
     * @return the validated User entity
     * @throws OfficerNotFoundException     if no user with the given ID exists
     * @throws ResourceNotFoundException    if the user does not have the required role
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    private User findOfficerById(UUID officerId) {
        try {
            User officer = userRepository.findById(officerId)
                    .orElseThrow(() -> new OfficerNotFoundException("Compliance Officer not found with id: " + officerId));
            if (!officer.getRoles().contains(Role.COMPLIANCE_OFFICER) && !officer.getRoles().contains(Role.ADMIN)) {
                throw new ResourceNotFoundException("User with id: " + officerId + " is not a compliance officer");
            }
            return officer;
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching compliance officer with ID {}: {}", officerId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve compliance officer");
        }
    }
}