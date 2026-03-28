package com.cts.edusphere.modules.work_load;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing the teaching workload assigned to a faculty member for a
 * specific course in the EduSphere system.
 *
 * <p>A {@code WorkLoad} entry records how many teaching hours a faculty member
 * ({@link User}) is responsible for in a given semester for a particular {@link Course}.
 * The assignment lifecycle is tracked via a {@link Status} flag.</p>
 *
 * <p>Records are persisted in the {@code work_load} table. Two indexes are defined to
 * support efficient filtering by faculty member ({@code faculty_id}) and by course
 * ({@code course_id}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code workload_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see User
 * @see Course
 * @see Status
 */
@Entity
@Table(
        name = "work_load",
        indexes = {
                @Index(name = "idx_workload_faculty", columnList = "faculty_id"),
                @Index(name = "idx_workload_course", columnList = "course_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "workload_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkLoad extends BaseEntity {

    /**
     * The faculty member ({@link User}) to whom this workload is assigned.
     * Lazily fetched; the join column {@code faculty_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private User faculty;

    /**
     * The course for which the workload hours are assigned.
     * Lazily fetched; the join column {@code course_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The total number of teaching hours allocated to the faculty member for
     * this course in the given semester. Must not be {@code null}.
     */
    @Column(nullable = false, name = "hours")
    private Integer hours;

    /**
     * A string identifying the academic semester to which this workload applies
     * (e.g., "Fall 2025", "Spring 2026"). Must not be {@code null}.
     */
    @Column(nullable = false, name = "semester")
    private String semester;

    /**
     * The current status of this workload assignment
     * (e.g., ACTIVE, COMPLETED, CANCELLED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
