package com.cts.edusphere.modules.courses;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.department.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing an academic course offered by the EduSphere institution.
 *
 * <p>A {@code Course} belongs to a single {@link Department} and carries information
 * about its credit value, duration, and current lifecycle {@link Status}. The title
 * of a course must be unique across the institution.</p>
 *
 * <p>Records are persisted in the {@code course} table. An index on {@code department_id}
 * supports fast retrieval of all courses belonging to a given department.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code course_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Department
 * @see Status
 */
@Entity
@Table(
        name = "course",
        indexes = {
                @Index(name = "idx_course_department", columnList = "department_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course extends BaseEntity {

    /**
     * The unique human-readable title of the course (e.g., "Introduction to Data Science").
     * Must be non-null and unique across all courses in the system.
     */
    @Column(nullable = false, unique = true)
    private String title;

    /**
     * The department that owns and administers this course.
     * Lazily fetched; excluded from JSON serialization to prevent circular references.
     * The join column {@code department_id} is non-nullable.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnore
    private Department department;

    /**
     * The number of academic credit units assigned to this course.
     * Must be a positive integer and cannot be {@code null}.
     */
    @Column(nullable = false)
    private int credits;

    /**
     * The duration of the course, typically expressed in weeks or hours
     * depending on the institutional convention. Must be a positive integer.
     */
    @Column(nullable = false)
    private int duration;

    /**
     * The current lifecycle status of this course (e.g., ACTIVE, INACTIVE, ARCHIVED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
