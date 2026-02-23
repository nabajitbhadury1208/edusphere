package com.cts.schlmgmt.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name= "FACULTY")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "FacultyID")

public class Faculty extends User{
        @Column(name = "FacultyID")
        @NotNull
        private int facultyId;

        @Column(name = "FacultyName")
        @NotNull
        private String facultyName;

        @Column(name = "DepartmentID")
        @NotNull
        private int departmentId;

        @Column(name = "Position")
        @NotNull
        private String position;

        @Column(name = "JoinDate")
        @NotNull
        private LocalDate joinDate;


}
