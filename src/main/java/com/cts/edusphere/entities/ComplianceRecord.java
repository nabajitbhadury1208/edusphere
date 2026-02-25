package com.cts.edusphere.entities;

import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

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
public class ComplianceRecord extends BaseEntity{
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_user_id", nullable = false)
    private ComplianceOfficer recordedBy;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType complianceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "result")
    private ComplianceResult result;

    @Column(nullable = false, name = "compliance_date")
    private LocalDate complianceDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

}
