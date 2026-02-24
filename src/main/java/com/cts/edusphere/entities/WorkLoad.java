package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "work_load")
@Getter
@Setter
@NoArgsConstructor
public class WorkLoad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID workloadId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Faculty faculty; // FK -> Faculty

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Course course; // FK -> Course

    @Column(nullable = false)
    private Integer hours;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String status;
}