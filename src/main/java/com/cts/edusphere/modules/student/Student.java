package com.cts.edusphere.modules.student;

import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity representing a student in the EduSphere system.
 *
 * <p>{@code Student} extends {@link User} using a JOINED inheritance strategy.
 * Student-specific columns are stored in the {@code students} table, joined to the
 * parent {@code users} table via the shared {@code user_id} primary key.</p>
 *
 * <p>Each student record stores demographic information (date of birth, gender,
 * and address) in addition to the enrollment date, which is set automatically
 * before the first persist if not supplied explicitly.</p>
 *
 * @see User
 * @see Gender
 */
@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")

public class Student extends User {

    /**
     * The student's date of birth. Must not be {@code null}.
     */
    @Column(nullable = false)
    private LocalDate dob;

    /**
     * The student's gender (e.g., MALE, FEMALE, OTHER).
     * Stored as a string; must not be {@code null}.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * The student's residential or mailing address.
     * Must not be {@code null}.
     */
    @Column(nullable = false)
    private String address;

    /**
     * The timestamp recording when the student was enrolled in the institution.
     * This field is non-nullable and immutable after the initial insert
     * ({@code updatable = false}). If not set before persistence, it is initialised
     * to the current instant by {@link #onPrePersist()}.
     */
    @Column(nullable = false, updatable = false, name = "enrollment_date")
    private Instant enrollmentDate;

    /**
     * JPA lifecycle callback invoked before a new {@code Student} entity is first
     * persisted to the database.
     *
     * <p>If {@link #enrollmentDate} has not been supplied explicitly, this method
     * initialises it to the current UTC instant, ensuring the enrollment timestamp
     * is always populated.</p>
     */
    @PrePersist
    protected void onPrePersist(){
        if (enrollmentDate == null){
            enrollmentDate = Instant.now();
        }
    }

}
