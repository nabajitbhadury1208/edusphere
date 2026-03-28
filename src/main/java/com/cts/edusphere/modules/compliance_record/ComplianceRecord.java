package com.cts.edusphere.modules.compliance_record;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity representing a compliance record within the EduSphere system.
 *
 * <p>A {@code ComplianceRecord} documents the outcome of a specific compliance check
 * carried out against any auditable entity (identified by a type string and a UUID).
 * The check is performed by a designated compliance officer, classified by a
 * {@link ComplianceType}, and results in a {@link ComplianceResult}.</p>
 *
 * <p>Records are persisted in the {@code compliance_records} table. Three indexes are
 * defined to optimise queries by the recording officer ({@code recorded_by_user_id}),
 * the target entity ({@code entity_id}), and the type of compliance check
 * ({@code compliance_type}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code compliance_id}), optimistic-locking version, and Spring Data
 * JPA auditing timestamps.</p>
 *
 * @see ComplianceType
 * @see ComplianceResult
 */
@Entity
@Table(
        name = "compliance_records",
        indexes = {
                @Index(name = "idx_compliance_recorded_by", columnList = "recorded_by_user_id"),
                @Index(name = "idx_compliance_entity_id", columnList = "entity_id"),
                @Index(name = "idx_compliance_type", columnList = "compliance_type"),
        }
)
@AttributeOverride(name = "id", column = @Column(name = "compliance_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ComplianceRecord extends BaseEntity {

    /**
     * The compliance officer ({@link User}) who recorded this compliance check.
     * Eagerness is set to LAZY for performance; the join column
     * {@code recorded_by_user_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_user_id", nullable = false)
    private User complianceOfficer;

    /**
     * UUID of the specific entity instance (e.g., department, course) that was
     * evaluated. Used in conjunction with {@link #entityType} to identify the
     * subject without hard foreign-key coupling.
     */
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    /**
     * String representation of the domain type of the entity being checked
     * (e.g., "Department", "Course", "Student"). Non-nullable.
     */
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    /**
     * The category of compliance being evaluated (e.g., ACADEMIC, FINANCIAL, HR).
     * Stored as a string; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType complianceType;

    /**
     * The outcome of the compliance evaluation (e.g., PASSED, FAILED, PENDING_REVIEW).
     * Stored as a string in the {@code result} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "result")
    private ComplianceResult result;

    /**
     * The calendar date on which the compliance check was performed.
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "compliance_date")
    private LocalDate complianceDate;

    /**
     * Optional free-text notes or remarks recorded by the compliance officer
     * regarding this check. Stored as a TEXT column to support verbose content.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

}
