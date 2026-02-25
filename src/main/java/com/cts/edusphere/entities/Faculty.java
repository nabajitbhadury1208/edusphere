package com.cts.edusphere.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.Instant;


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
        private Department department;

        @Column(nullable = false)
        private String position;

        @Column(nullable = false, updatable = false, name = "join_date")
        private Instant joinDate;



}
