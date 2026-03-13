package com.cts.edusphere.modules.audit;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "audits", indexes = {@Index(name = "idx_audit_officer", columnList = "user_id")})
@AttributeOverride(name = "id", column = @Column(name = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Audit extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User complianceOfficer;

    @Column(nullable = false, name = "scope")
    private String scope;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(nullable = false, name = " ")
    private LocalDate auditDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private AuditStatus status;
}
