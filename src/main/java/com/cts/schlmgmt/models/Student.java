package com.cts.schlmgmt.models;

import com.cts.schlmgmt.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "STUDENT")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentID")
    private UUID studentId;

    @Column(name = "Name")
    @NotNull
    private String name;

    @Column(name = "DOB")
    @NotNull
    private LocalDate dob;

    @Column(name = "Gender")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "Address")
    @NotNull
    private String address;

    @Column(name = "ContactInfo")
    @NotNull
    private long contactInfo;

    @Column(name = "EnrollmentDate")
    @NotNull
    private LocalDateTime enrollmentDate;

    @Column(name = "Status")
    @NotNull
    private boolean status;
}
