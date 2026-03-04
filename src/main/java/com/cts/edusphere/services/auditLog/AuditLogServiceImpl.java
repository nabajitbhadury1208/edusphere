package com.cts.edusphere.services.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditLogMapper;
import com.cts.edusphere.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public List<AuditLogResponseDTO> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuditLogResponseDTO getLogById(UUID id) {
        return auditLogRepository.findById(id)
                .map(auditLogMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByUser(UUID userId) {
        // FIX: Call findByUser_Id
        return auditLogRepository.findByUser_Id(userId).stream()
                .map(auditLogMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByResource(String resource) {
        return auditLogRepository.findByResourceContainingIgnoreCase(resource).stream()
                .map(auditLogMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}