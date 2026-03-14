package com.cts.edusphere.services.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.mappers.audit_log.AuditLogMapper;
import com.cts.edusphere.modules.audit_log.AuditLog;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.audit_log.AuditLogRepository;
import com.cts.edusphere.repositories.user.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String createLog(AuditLogRequestDTO auditLogRequestDTO, UUID userId) {
        try {
            if (userId == null) {
                log.warn("Cannot create audit log: userId is null. (Action: {})", auditLogRequestDTO.action());
                return "Skipped audit: No user context";
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for creation of audit log"));
            AuditLog auditLog = auditLogMapper.toEntity(auditLogRequestDTO, user);
            auditLogRepository.save(auditLog);
            return "Successfully created audit";
        } catch (Exception e) {
            log.error("Error occurred while creating audit log: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to save audit log");
        }
    }


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