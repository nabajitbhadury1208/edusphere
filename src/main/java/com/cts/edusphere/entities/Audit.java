package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "audits")
@Getter
@Setter
@NoArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID auditId;

    // OfficerID in the diagram; kept scalar to avoid assuming a specific role/entity
    @Column(nullable = false)
    private UUID officerId;

    @Column(nullable = false)
    private String scope;

    @Column(length = 4000)
    private String findings;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String status;
}
