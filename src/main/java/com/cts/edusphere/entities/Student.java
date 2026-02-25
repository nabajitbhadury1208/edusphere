package com.cts.edusphere.entities;

import com.cts.edusphere.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")

public class Student extends User {
    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, updatable = false, name = "enrollment_date")
    private Instant enrollmentDate;

    @PrePersist
    protected void onPrePersist(){
        if (enrollmentDate == null){
            enrollmentDate = Instant.now();
        }
    }

}
