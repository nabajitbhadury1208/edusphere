package com.cts.edusphere.services.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.audit_log.AuditLogMapper;
import com.cts.edusphere.modules.audit_log.AuditLog;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.audit_log.AuditLogRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    @Autowired
    public AuditLogServiceImpl(
            @Lazy AuditLogRepository auditLogRepository,
            AuditLogMapper auditLogMapper,
            UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void logSystemEvent(SystemLogType logType, Severity severity,
                               String action, String resource,
                               String details, UUID userId) {
        try {
            User user = null;
            if (userId != null) {
                user = userRepository.findById(userId).orElse(null);
            }

            AuditLog log = AuditLog.builder()
                    .user(user)
                    .action(action != null ? action : "SYSTEM")
                    .resource(resource != null ? resource : "SYSTEM")

                    .logType(logType)
                    .severity(severity != null ? severity : Severity.INFO)
                    .details(details)
                    .build();

            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("Failed to persist system audit log: {}", e.getMessage());
        }
    }

    @Override
    public List<AuditLogResponseDTO> getAllLogs() {
        try {
            return auditLogRepository.findAll().stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all audit logs: {}", e.getMessage());
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
            log.error("Error fetching audit log {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit log");
        }
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByUser(UUID userId) {
        try {
            return auditLogRepository.findByUser_Id(userId).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching audit logs for user {}: {}", userId, e.getMessage());
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
            log.error("Error fetching audit logs for resource {}: {}", resource, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve resource audit logs");
        }
    }

    @Override
    public List<AuditLogResponseDTO> getLogsBySeverity(Severity severity) {
        try {
            return auditLogRepository.findBySeverity(severity).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching audit logs by severity {}: {}", severity, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs by severity");
        }
    }

    @Override
    public List<AuditLogResponseDTO> getLogsByType(SystemLogType logType) {
        try {
            return auditLogRepository.findByLogType(logType).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching audit logs by type {}: {}", logType, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs by type");
        }
    }
}