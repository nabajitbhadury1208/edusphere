package com.cts.schlmgmt.models;

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
@Table(name="ComplianceOfficer")
@PrimaryKeyJoinColumn(name = "ComplianceOfficerID")
public class ComplianceOfficer extends User{

    @Column(name = "ComplianceOfficerId")
    @NotNull
    private int complianceOfficerId;



}