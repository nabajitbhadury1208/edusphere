package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "exam")
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID examId;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @Column()
    private String type;

    @Column()
    private LocalDate date;

    @Column()
    private Boolean status;
}
