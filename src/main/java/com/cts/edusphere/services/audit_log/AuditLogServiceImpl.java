package com.cts.edusphere.services.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.AuditLogNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.AuditLogsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.FailedToCreateLogException;
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

/**
 * Service implementation for system audit logging.
 * Persists structured log entries for all API actions and system events.
 * Uses @Lazy injection for the repository to prevent circular dependency issues.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    /**
     * Constructor with @Lazy AuditLogRepository injection to prevent circular dependency.
     *
     * @param auditLogRepository the lazily-loaded repository for audit log persistence
     * @param auditLogMapper     the mapper for converting entities to response DTOs
     * @param userRepository     the repository for resolving user entities by ID
     */
    @Autowired
    public AuditLogServiceImpl(
            @Lazy AuditLogRepository auditLogRepository,
            AuditLogMapper auditLogMapper,
            UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
        this.userRepository = userRepository;
    }


    /**
     * Persists a structured system event to the audit log.
     * Optionally associates the log entry with a user if userId is provided.
     * Applies default values: "SYSTEM" for null action/resource, INFO for null severity.
     * Non-throwing by design — swallows exceptions to prevent logging failures
     * from disrupting business operations.
     *
     * @param logType   the SystemLogType classifying the event (e.g., API_ACCESS, INTERNAL_ERROR)
     * @param severity  the Severity level (INFO, WARN, ERROR); defaults to INFO if null
     * @param action    the name of the action or method; defaults to "SYSTEM" if null
     * @param resource  the name of the resource or class involved; defaults to "SYSTEM" if null
     * @param details   additional detail string (e.g., exception message); may be null
     * @param userId    the UUID of the associated user; may be null for system-level events
     */
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
        } catch (FailedToCreateLogException e) {
            log.error("Failed to persist system audit log: {}", e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while logging system event: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to log system event");
        }
    }

    /**
     * Retrieves all audit log entries from the database.
     *
     * @return a list of all AuditLogResponseDTO objects
     * @throws AuditLogsNotFoundException   if no log entries exist
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditLogResponseDTO> getAllLogs() {
        try {
            return auditLogRepository.findAll().stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditLogsNotFoundException e) {
            log.error("Error fetching all audit logs: {}", e.getMessage());
            throw new AuditLogsNotFoundException("Failed to retrieve audit logs");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all audit logs: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs");
        }
    }

    /**
     * Retrieves a specific audit log entry by its unique identifier.
     *
     * @param id the UUID of the log entry to retrieve
     * @return the matching AuditLogResponseDTO
     * @throws AuditLogNotFoundException    if no log entry with the given ID exists
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public AuditLogResponseDTO getLogById(UUID id) {
        try {
            return auditLogRepository.findById(id)
                    .map(auditLogMapper::toResponseDTO)
                    .orElseThrow(() -> new AuditLogNotFoundException("Audit log not found with id: " + id));
        // } catch (ResourceNotFoundException e) {
        //     throw e;
        } catch (AuditLogNotFoundException e) {
            log.error("Error fetching audit log {}: {}", id, e.getMessage());
            throw new AuditLogNotFoundException("Failed to retrieve audit log");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit log {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit log");
        }
    }

    /**
     * Retrieves all audit log entries associated with a specific user.
     *
     * @param userId the UUID of the user whose log entries are to be retrieved
     * @return a list of AuditLogResponseDTO objects for the given user
     * @throws AuditLogsNotFoundException   if no log entries exist for the user
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditLogResponseDTO> getLogsByUser(UUID userId) {
        try {
            return auditLogRepository.findByUser_Id(userId).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditLogsNotFoundException e) {
            log.error("Error fetching audit logs for user {}: {}", userId, e.getMessage());
            throw new AuditLogsNotFoundException("Failed to retrieve user audit logs");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit logs for user {}: {}", userId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve user audit logs");
        }
    }

    /**
     * Retrieves audit log entries where the resource field contains the given string
     * (case-insensitive partial match).
     *
     * @param resource the resource name to search for
     * @return a list of matching AuditLogResponseDTO objects
     * @throws AuditLogsNotFoundException   if no matching entries exist
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditLogResponseDTO> getLogsByResource(String resource) {
        try {
            return auditLogRepository.findByResourceContainingIgnoreCase(resource).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditLogsNotFoundException e) {
            log.error("Error fetching audit logs for resource {}: {}", resource, e.getMessage());
            throw new AuditLogsNotFoundException("Failed to retrieve resource audit logs");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit logs for resource {}: {}", resource, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve resource audit logs");
        }
    }

    /**
     * Retrieves all audit log entries with a specific severity level.
     *
     * @param severity the Severity enum value to filter by (INFO, WARN, ERROR)
     * @return a list of AuditLogResponseDTO objects matching the given severity
     * @throws AuditLogsNotFoundException   if no entries exist for the given severity
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditLogResponseDTO> getLogsBySeverity(Severity severity) {
        try {
            return auditLogRepository.findBySeverity(severity).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditLogsNotFoundException e) {
            log.error("Error fetching audit logs by severity {}: {}", severity, e.getMessage());
            throw new AuditLogsNotFoundException("Failed to retrieve audit logs by severity");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit logs by severity {}: {}", severity, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs by severity");
        }
    }

    /**
     * Retrieves all audit log entries with a specific log type.
     *
     * @param logType the SystemLogType enum value to filter by
     * @return a list of AuditLogResponseDTO objects matching the given log type
     * @throws AuditLogsNotFoundException   if no entries exist for the given log type
     * @throws InternalServerErrorException if an unexpected error occurs
     */
    @Override
    public List<AuditLogResponseDTO> getLogsByType(SystemLogType logType) {
        try {
            return auditLogRepository.findByLogType(logType).stream()
                    .map(auditLogMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (AuditLogsNotFoundException e) {
            log.error("Error fetching audit logs by type {}: {}", logType, e.getMessage());
            throw new AuditLogsNotFoundException("Failed to retrieve audit logs by type");
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching audit logs by type {}: {}", logType, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve audit logs by type");
        }
    }
}