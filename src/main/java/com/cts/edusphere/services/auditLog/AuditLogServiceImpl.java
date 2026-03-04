package com.cts.edusphere.services.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditLogMapper;
import com.cts.edusphere.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public List<AuditLogResponseDTO> getAllLogs() {
        try {
            return auditLogRepository.findAll().stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching all audit logs: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs");
        }
    }

    @Override
    public AuditLogResponseDTO getLogById(UUID id) {
        try {
            return auditLogRepository.findById(id)
                    .map(auditLogMapper::toResponseDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while fetching audit log with ID {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit log entry");
        }
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByUser(UUID userId) {
        try {
            return auditLogRepository.findByUser_Id(userId).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching audit logs for user {}: {}", userId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve user audit logs");
        }
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByResource(String resource) {
        try {
            return auditLogRepository.findByResourceContainingIgnoreCase(resource).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching audit logs for resource {}: {}", resource, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve resource audit logs");
        }
    }
}