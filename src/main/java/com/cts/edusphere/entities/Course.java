package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "course")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID courseId;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private String departmentId;

    @Column
    private int credits;

    @Column
    private int duration;

    @Column
    private boolean status;
}
