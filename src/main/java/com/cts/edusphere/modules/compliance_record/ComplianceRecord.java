package com.cts.edusphere.modules.compliance_record;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ComplianceEntityType;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.modules.user.User;
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
                @Index(name = "idx_compliance_entity_id", columnList = "entity_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "compliance_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ComplianceRecord extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_user_id", nullable = false)
    private User complianceOfficer;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private ComplianceEntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "result")
    private ComplianceResult result;

    @Column(nullable = false, name = "compliance_date")
    private LocalDate complianceDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

}
