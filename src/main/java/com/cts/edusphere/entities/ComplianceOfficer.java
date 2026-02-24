package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="compliance_officer")
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)

public class ComplianceOfficer extends User{
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }



}
