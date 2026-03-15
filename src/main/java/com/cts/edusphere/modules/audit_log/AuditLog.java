package com.cts.edusphere.modules.audit_log;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true, referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "log_type")
    private SystemLogType logType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "severity")
    private Severity severity;

    @Column(columnDefinition = "TEXT", name = "details")
    private String details;

}
