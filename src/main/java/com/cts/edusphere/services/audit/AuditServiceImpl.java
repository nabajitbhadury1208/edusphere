package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditMapper;
import com.cts.edusphere.modules.Audit;
import com.cts.edusphere.modules.ComplianceOfficer;
import com.cts.edusphere.repositories.AuditRepository;
import com.cts.edusphere.repositories.ComplianceOfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final ComplianceOfficerRepository officerRepository;
    private final AuditMapper auditMapper;

    @Override
    public AuditResponseDTO createAudit(AuditRequestDTO dto) {
        ComplianceOfficer officer = findOfficerById(dto.officerId());

        Audit audit = auditMapper.toEntity(dto);
        audit.setOfficer(officer);

        return auditMapper.toResponseDTO(auditRepository.save(audit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAllAudits() {
        return auditRepository.findAll().stream()
                .map(auditMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuditResponseDTO getAuditById(UUID id) {
        return auditRepository.findById(id)
                .map(auditMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Audit record not found with id: " + id));
    }

    @Override
    public AuditResponseDTO updateAudit(UUID id, AuditRequestDTO dto) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit record not found with id: " + id));

        // Update fields
        audit.setScope(dto.scope());
        audit.setFindings(dto.findings());
        audit.setAuditDate(LocalDate.parse(dto.auditDate()));

        if (!audit.getOfficer().getId().equals(dto.officerId())) {
            audit.setOfficer(findOfficerById(dto.officerId()));
        }

        return auditMapper.toResponseDTO(auditRepository.save(audit));
    }

    @Override
    public void deleteAudit(UUID id) {
        if (!auditRepository.existsById(id)) {
            throw new ResourceNotFoundException("Audit record not found with id: " + id);
        }
        auditRepository.deleteById(id);
    }

    private ComplianceOfficer findOfficerById(UUID officerId) {
        return officerRepository.findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance Officer not found with id: " + officerId));
    }
}