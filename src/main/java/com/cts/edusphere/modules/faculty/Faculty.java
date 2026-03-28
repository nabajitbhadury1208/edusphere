package com.cts.edusphere.modules.faculty;

import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.modules.department.Department;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;


/**
 * JPA entity representing a faculty member in the EduSphere system.
 *
 * <p>{@code Faculty} extends {@link User} using a JOINED inheritance strategy.
 * The faculty-specific columns are stored in the {@code faculty_members} table,
 * joined to the parent {@code users} table via the shared {@code user_id} primary key.</p>
 *
 * <p>Each faculty member belongs to a {@link Department}, holds a named position
 * (e.g., "Associate Professor"), and records the date they joined the institution.
 * The join date is set automatically before the first persist if it has not been
 * supplied explicitly.</p>
 *
 * @see User
 * @see Department
 */
@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "faculty_members")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")

public class Faculty extends User {

    /**
     * The department to which this faculty member is assigned.
     * Lazily fetched; the join column {@code department_id} is non-nullable.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * The academic or administrative position held by this faculty member
     * (e.g., "Professor", "Lecturer", "Department Coordinator").
     * Must not be {@code null}.
     */
    @Column(nullable = false)
    private String position;

    /**
     * The timestamp recording when the faculty member first joined the institution.
     * This field is non-nullable and immutable after the initial insert
     * ({@code updatable = false}). If not set before persistence, it is initialised
     * to the current instant by {@link #onPrePersist()}.
     */
    @Column(nullable = false, updatable = false, name = "join_date")
    private Instant joinDate;

    /**
     * JPA lifecycle callback invoked before a new {@code Faculty} entity is first
     * persisted to the database.
     *
     * <p>If {@link #joinDate} has not been set explicitly, this method initialises
     * it to the current UTC instant, ensuring the field is always populated with a
     * meaningful timestamp.</p>
     */
    @PrePersist
    protected void onPrePersist() {
        if (joinDate == null) {
            joinDate = Instant.now();
        }
    }

}
