package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "researchProjects")
@Getter
@Setter
@NoArgsConstructor
public class ResearchProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id", nullable = false, updatable = false)
    private UUID projectId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty; // FK -> Faculty

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // FK -> Student

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    private String status;
}
