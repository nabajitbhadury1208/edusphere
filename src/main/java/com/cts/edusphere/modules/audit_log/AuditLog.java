package com.cts.edusphere.modules.audit_log;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "audit_log",
        indexes = {
                @Index(name = "idx_audit_log_user", columnList = "user_id"),
                @Index(name = "idx_audit_log_resource", columnList = "resource")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "audit_log_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class AuditLog extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resource;

}
