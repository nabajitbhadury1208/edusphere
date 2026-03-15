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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User complianceOfficer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "entity_type")
    private AuditEntityType entityType;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(nullable = false, name = "scope")
    private String scope;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private AuditStatus status;
}
