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

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "ADMIN")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "AdminID")
public class Administrator extends User{

    @Column(name = "AdminID")
    @NotNull
    private int adminId;
}
