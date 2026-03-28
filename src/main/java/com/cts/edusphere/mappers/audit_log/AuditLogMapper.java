package com.cts.edusphere.mappers.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.modules.audit_log.AuditLog;

import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link AuditLog} entity objects
 * and their corresponding DTO representations ({@link AuditLogRequestDTO} and
 * {@link AuditLogResponseDTO}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever audit log
 * mapping is required. Default values are applied for {@code action}, {@code resource},
 * and {@code severity} when the corresponding fields in the request are {@code null}.</p>
 */
@Component
public class AuditLogMapper {

    /**
     * Converts an {@link AuditLog} entity to an {@link AuditLogResponseDTO}.
     *
     * <p>The user's ID is extracted safely; if no user is associated with the log entry,
     * the corresponding field in the response will be {@code null}.</p>
     *
     * @param entity the {@link AuditLog} entity to convert
     * @return an {@link AuditLogResponseDTO} populated with data from the entity
     */
    public AuditLogResponseDTO toResponseDTO(AuditLog entity) {
        return new AuditLogResponseDTO(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getAction(),
                entity.getResource(),
                entity.getCreatedAt(),
                entity.getDetails(),
                entity.getLogType(),
                entity.getSeverity()
        );
    }

    /**
     * Converts an {@link AuditLogRequestDTO} and a resolved {@link User} entity to an
     * {@link AuditLog} entity.
     *
     * <p>If {@code action} or {@code resource} in the request are {@code null}, they default
     * to {@code "SYSTEM"}. If {@code severity} is {@code null}, it defaults to
     * {@link Severity#INFO}. The {@link User} object is provided separately because the
     * request DTO only carries an identifier; the caller is responsible for resolving
     * the user beforehand.</p>
     *
     * @param auditLogRequestDTO the {@link AuditLogRequestDTO} containing audit log data
     * @param user               the {@link User} entity to associate with the audit log entry
     * @return a new {@link AuditLog} entity built from the request data and user
     */
    public AuditLog toEntity(AuditLogRequestDTO auditLogRequestDTO, User user) {
        return AuditLog
                .builder()
                .user(user)
                .action(auditLogRequestDTO.action() != null ? auditLogRequestDTO.action() : "SYSTEM")
                .resource(auditLogRequestDTO.resource() != null ? auditLogRequestDTO.resource() : "SYSTEM")
                .logType(auditLogRequestDTO.logType())
                .severity(auditLogRequestDTO.severity() != null ? auditLogRequestDTO.severity() : Severity.INFO)
                .details(auditLogRequestDTO.details())
                .build();

    }
}
