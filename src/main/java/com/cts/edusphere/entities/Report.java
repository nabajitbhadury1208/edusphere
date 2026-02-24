package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="report")
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID reportId;

    @Column
    private String scope;

    @Column
    private String metrics;

    @Column
    private LocalDateTime generatedDate;
}
