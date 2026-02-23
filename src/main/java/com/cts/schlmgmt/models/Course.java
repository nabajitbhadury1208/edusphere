package com.cts.schlmgmt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "COURSE")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CourseId")
    private UUID courseId;

    @Column(name = "Title")
    @NotNull
    private String title;

    @Column(name = "DepartmentID")
    @ManyToOne
    @JoinColumn(name = "")
    private String departmentId;
    private int credits;
    private int duration;
    private boolean status;
}
