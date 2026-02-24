package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "thesis")
@Getter
@Setter
@NoArgsConstructor
public class Thesis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID thesisId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Student student; // FK -> Student

    @Column(nullable = false)
    private String title;

    // Diagram only lists SupervisorID; keeping it scalar to avoid assumptions
    @Column(nullable = false)
    private UUID supervisorId;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String status;
}