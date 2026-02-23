package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "workLoad")
@Getter
@Setter
@NoArgsConstructor
public class WorkLoad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "workload_id", nullable = false, updatable = false)
    private UUID workloadId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty; // FK -> Faculty

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // FK -> Course

    @Column(name = "hours", nullable = false)
    private Integer hours;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "status", nullable = false)
    private String status;
}