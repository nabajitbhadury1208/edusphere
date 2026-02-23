package com.cts.schlmgmt.models;

import com.cts.schlmgmt.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "STUDENT")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "StudentID")
public class Student extends User {
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
}
