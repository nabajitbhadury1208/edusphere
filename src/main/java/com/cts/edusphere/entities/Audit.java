package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "audits")
@AttributeOverride(name = "id", column = @Column(name = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Audit extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ComplianceOfficer officer;

    @Column(nullable = false, name = "scope")
    private String scope;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(nullable = false, name = "audit_date")
    private LocalDate auditDate;

    @Column(nullable = false, name = "status")
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
