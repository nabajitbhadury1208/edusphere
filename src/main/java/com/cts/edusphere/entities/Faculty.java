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
@Table(name= "faculty_members")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")

public class Faculty extends User{
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "department_id", nullable = false)

        @Column(nullable = false)
        private String position;

        @Column(nullable = false, updatable = false, name = "join_date")
        private Instant joinDate;



}
