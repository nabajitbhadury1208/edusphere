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
    @Column(name = "thesis_id", nullable = false, updatable = false)
    private UUID thesisId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // FK -> Student

    @Column(name = "title", nullable = false)
    private String title;

    // Diagram only lists SupervisorID; keeping it scalar to avoid assumptions
    @Column(name = "supervisor_id", nullable = false)
    private UUID supervisorId;

    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @Column(name = "status", nullable = false)
    private String status;
}