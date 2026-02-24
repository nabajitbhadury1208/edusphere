package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "department")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "department_id"))
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Department extends BaseEntity {
    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "department_code", nullable = false)
    private String departmentCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_id", nullable = false)
    private Faculty headId;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
