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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final UserRepository userRepository;

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