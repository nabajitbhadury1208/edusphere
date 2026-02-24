package com.cts.edusphere.entities;

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
@Table(name = "compliance_records")
@AttributeOverride(name = "id", column = @Column(name = "compliance_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ComplianceRecord extends BaseEntity{
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType complianceType;

    @Column(nullable = false, name = "result")
    private String result;

    @Column(nullable = false, name = "compliance_date")
    private LocalDate complianceDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

}
