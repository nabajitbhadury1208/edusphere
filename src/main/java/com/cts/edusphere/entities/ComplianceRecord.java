package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "complianceRecords")
@Getter
@Setter
@NoArgsConstructor
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "compliance_id", nullable = false, updatable = false)
    private UUID complianceId;

    // Kept scalar because the diagram models a generic "EntityID"
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "notes", length = 4000)
    private String notes;
}
