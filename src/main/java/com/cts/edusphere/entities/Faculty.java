package com.cts.edusphere.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name= "faculty")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "facultyId")
@EntityListeners(AuditingEntityListener.class)

public class Faculty extends User{
        @Column(nullable = false)
        private int facultyId;

        @Column(nullable = false)
        private String facultyName;

        @Column(nullable = false)
        private int departmentId;

        @Column(nullable = false)
        private String position;

        @Column(nullable = false)
        private LocalDate joinDate;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
