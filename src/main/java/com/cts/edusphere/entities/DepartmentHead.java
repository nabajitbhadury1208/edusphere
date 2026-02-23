package com.cts.edusphere.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="departmenthead")
@PrimaryKeyJoinColumn(name = "departmentHeadID")
@EntityListeners(AuditingEntityListener.class)

public class DepartmentHead extends User{

    @Column(nullable = false)
    private LocalDate departmentHeadId;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
