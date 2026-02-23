package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Data

@Table(name="curriculum")
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false )
    private UUID curriculumID;

    @ManyToOne
    @JoinColumn(name = "CourseId")
    private Course course;

    @Column()
    private String description;

    // have to check this one by Nabajit here in this case the datatype needs to be fixed
    @Column(name = "ModulesJSON")
    private String modulesJSON;

    @Column()
    private Boolean status;



}
