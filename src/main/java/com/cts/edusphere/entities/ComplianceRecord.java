package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "compliance_records")
@Getter
@Setter
@NoArgsConstructor
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID complianceId;

    // Kept scalar because the diagram models a generic "EntityID"
    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String result;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 4000)
    private String notes;
}
