package com.cts.edusphere.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="complianceofficer")
@PrimaryKeyJoinColumn(name = "complianceOfficerID")
@EntityListeners(AuditingEntityListener.class)

public class ComplianceOfficer extends User{

    @Column(nullable = false)
    private int complianceOfficerId;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }



}
