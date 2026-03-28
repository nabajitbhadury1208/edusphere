package com.cts.edusphere.modules.audit_log;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing a system-level audit log entry in the EduSphere application.
 *
 * <p>An {@code AuditLog} record captures a single action performed on a system resource,
 * together with the user who performed it, the type of log event, and the severity of
 * the action. It is intended for security auditing, compliance reporting, and operational
 * monitoring.</p>
 *
 * <p>Records are persisted in the {@code audit_log} table. Three indexes are defined to
 * support efficient filtering by user ({@code user_id}), log type ({@code log_type}),
 * and severity ({@code severity}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary key
 * (mapped to {@code audit_log_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see SystemLogType
 * @see Severity
 */
@Entity
@Table(
        name = "audit_log",
        indexes = {
                @Index(name = "idx_audit_log_user", columnList = "user_id"),
                @Index(name = "idx_audit_log_log_type", columnList = "log_type"),
                @Index(name = "idx_audit_log_severity", columnList = "severity")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "audit_log_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class AuditLog extends BaseEntity {

    /**
     * The user who performed the logged action.
     * This association is nullable because certain system-generated events
     * (e.g., scheduled jobs) may not be associated with a specific user.
     * Lazily fetched; join column {@code user_id} references the {@code users} table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true, referencedColumnName = "user_id")
    private User user;

    /**
     * A short description of the action that was performed
     * (e.g., "LOGIN", "DELETE_STUDENT", "EXPORT_REPORT").
     * Must not be {@code null}.
     */
    @Column(nullable = false)
    private String action;

    /**
     * The system resource or entity on which the action was performed
     * (e.g., "User", "Course", "Grade").
     * Must not be {@code null}.
     */
    @Column(nullable = false)
    private String resource;

    /**
     * Categorises the log entry by the type of system event it represents
     * (e.g., SECURITY, DATA_CHANGE, ACCESS).
     * Stored as a string in the {@code log_type} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "log_type")
    private SystemLogType logType;

    /**
     * The severity level of the logged action (e.g., INFO, WARNING, CRITICAL).
     * Stored as a string in the {@code severity} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "severity")
    private Severity severity;

    /**
     * Optional free-text field providing additional context or a full payload
     * related to the action. Stored as a TEXT column to support large content.
     */
    @Column(columnDefinition = "TEXT", name = "details")
    private String details;

}
