package com.cts.edusphere.entities;
import com.cts.edusphere.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="Regulator")
@PrimaryKeyJoinColumn(name = "RegulatorID")
public class Regulator extends User {

    @Column(name = "RegulatorId")
    @NotNull
    private int regulatorId;


}
