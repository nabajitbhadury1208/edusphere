package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditMapper;
import com.cts.edusphere.modules.Audit;
import com.cts.edusphere.modules.ComplianceOfficer;
import com.cts.edusphere.repositories.AuditRepository;
import com.cts.edusphere.repositories.ComplianceOfficerRepository;
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
    private final ComplianceOfficerRepository officerRepository;
    private final AuditMapper auditMapper;

    @Override
    @Transactional
    public AuditResponseDTO createAudit(AuditRequestDTO dto) {
        try {
            ComplianceOfficer officer = findOfficerById(dto.officerId());

            Audit audit = auditMapper.toEntity(dto);
            audit.setOfficer(officer);

            Audit savedAudit = auditRepository.save(audit);
            log.info("Audit record created successfully with ID: {}", savedAudit.getId());
            return auditMapper.toResponseDTO(savedAudit);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating audit record: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create audit record");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAllAudits() {
        try {
            return auditRepository.findAll().stream()
                    .map(auditMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching all audit records: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit records");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuditResponseDTO getAuditById(UUID id) {
        try {
            return auditRepository.findById(id)
                    .map(auditMapper::toResponseDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Audit record not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while fetching audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit record");
        }
    }

    @Override
    @Transactional
    public AuditResponseDTO updateAudit(UUID id, AuditRequestDTO dto) {
        try {
            Audit audit = auditRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Audit record not found with id: " + id));

            // Update fields
            audit.setScope(dto.scope());
            audit.setFindings(dto.findings());
            audit.setAuditDate(LocalDate.parse(dto.auditDate()));

            if (!audit.getOfficer().getId().equals(dto.officerId())) {
                audit.setOfficer(findOfficerById(dto.officerId()));
            }

            Audit updatedAudit = auditRepository.save(audit);
            log.info("Audit record with ID {} updated successfully", id);
            return auditMapper.toResponseDTO(updatedAudit);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update audit record");
        }
    }

    @Override
    @Transactional
    public void deleteAudit(UUID id) {
        try {
            if (!auditRepository.existsById(id)) {
                throw new ResourceNotFoundException("Audit record not found with id: " + id);
            }
            auditRepository.deleteById(id);
            log.info("Audit record with ID {} deleted successfully", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while deleting audit record with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete audit record");
        }
    }

    private ComplianceOfficer findOfficerById(UUID officerId) {
        // This is called within other try-catch blocks, but we throw specific exception here
        return officerRepository.findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance Officer not found with id: " + officerId));
    }
}