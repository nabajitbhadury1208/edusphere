package com.cts.edusphere.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="DepartmentHead")
@PrimaryKeyJoinColumn(name = "DepartmentHeadID")
public class DepartmentHead extends User{

    @Column(name = "DepartmentHeadID")
    @NotNull
    private LocalDate departmentHeadId;


}
