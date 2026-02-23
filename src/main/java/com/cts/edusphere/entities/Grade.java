package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "grade_id", nullable = false, updatable = false)
    private UUID gradeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam; // FK -> Exam

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // FK -> Student

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "status", nullable = false)
    private String status;
}