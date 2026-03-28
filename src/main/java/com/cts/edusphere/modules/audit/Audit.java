package com.cts.edusphere.modules.audit;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity representing an audit record within the EduSphere system.
 *
 * <p>An {@code Audit} captures the result of a compliance or quality audit
 * performed by a designated compliance officer against a specific entity
 * (e.g., a department, course, or student record). Each audit has a defined
 * scope, a date on which it was conducted, a textual summary of findings, and
 * a lifecycle {@link AuditStatus}.</p>
 *
 * <p>Records are persisted in the {@code audits} table. Two database indexes
 * are defined to accelerate lookups by officer ({@code user_id}) and by the
 * type of entity being audited ({@code entity_type}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID
 * primary key (mapped to {@code audit_id}), optimistic-locking version, and
 * Spring Data JPA auditing timestamps.</p>
 *
 * @see AuditEntityType
 * @see AuditStatus
 */
@Entity
@Table(
        name = "audits",
        indexes = {
                @Index(name = "idx_audit_officer", columnList = "user_id"),
                @Index(name = "idx_audit_type", columnList = "entity_type")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Audit extends BaseEntity {

    /**
     * The compliance officer ({@link User}) who conducted this audit.
     * Lazily fetched; join column {@code user_id} references the {@code users}
     * table. Indexed for efficient lookups by officer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User complianceOfficer;

    /**
     * The type of domain entity that was audited (e.g., DEPARTMENT, COURSE).
     * Stored as a string in the {@code entity_type} column.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "entity_type")
    private AuditEntityType entityType;

    /**
     * UUID of the specific entity instance that was audited.
     * Combined with {@link #entityType}, this uniquely identifies the audited
     * object without requiring a hard foreign-key reference to every possible
     * entity table.
     */
    @Column(name = "entity_id")
    private UUID entityId;

    /**
     * A short description of the audit scope (e.g., "Financial compliance Q1").
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "scope")
    private String scope;

    /**
     * Free-text findings recorded by the compliance officer during the audit.
     * Stored as a TEXT column to accommodate large narrative content.
     */
    @Column(columnDefinition = "TEXT")
    private String findings;

    /**
     * The calendar date on which the audit was conducted.
     */
    @Column(name = "audit_date")
    private LocalDate auditDate;

    /**
     * The current lifecycle status of this audit (e.g., PENDING, COMPLETED,
     * FAILED). Stored as a string in the {@code status} column.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private AuditStatus status;
}
