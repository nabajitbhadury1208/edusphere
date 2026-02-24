package com.cts.edusphere.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name= "faculty")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)

public class Faculty extends User{
        @Column(nullable = false, name = "department_id")
        private UUID departmentId;

        @Column(nullable = false)
        private String position;

        @CreatedDate
        private Instant joinDate;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
