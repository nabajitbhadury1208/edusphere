package com.cts.edusphere.modules.department;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.modules.courses.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * JPA entity representing an academic department within the EduSphere institution.
 *
 * <p>A {@code Department} groups a set of {@link Faculty} members and {@link Course}
 * offerings under a shared organisational unit. Each department has a unique
 * alphanumeric code, an optional head of department (a {@link User}), and a lifecycle
 * {@link Status}.</p>
 *
 * <p>Records are persisted in the {@code department} table. A unique constraint is
 * enforced on {@code department_code}, and an index on {@code head_id} supports fast
 * lookup of departments by their assigned head.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code department_id}), optimistic-locking version, and Spring Data
 * JPA auditing timestamps.</p>
 *
 * @see Faculty
 * @see Course
 * @see Status
 */
@Entity
@Table(
        name = "department",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_department_code", columnNames = "department_code")
        }, indexes = @Index(name = "idx_department_head", columnList = "head_id")
)
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "department_id"))
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Department extends BaseEntity {

    /**
     * The full display name of the department (e.g., "Computer Science and Engineering").
     * Must not be {@code null}.
     */
    @Column(name = "department_name", nullable = false)
    private String departmentName;

    /**
     * A short, institution-unique alphanumeric code identifying the department
     * (e.g., "CSE", "MECH"). Subject to a unique constraint at the database level.
     */
    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    /**
     * The user designated as the head of this department.
     * This is an optional one-to-one association; a department may temporarily
     * have no assigned head. Lazily fetched.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "head_id", nullable = true)
    private User departmentHead;

    /**
     * Contact information for the department (e.g., phone number or email address).
     * Must not be {@code null}.
     */
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    /**
     * The current operational status of this department (e.g., ACTIVE, INACTIVE).
     * Stored as a string; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * The list of faculty members who belong to this department.
     * Mapped by the {@code department} field in {@link Faculty}.
     * All cascade operations are propagated to the faculty members.
     * Lazily fetched to avoid unnecessary joins.
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Faculty> faculties;

    /**
     * The list of courses offered by this department.
     * Mapped by the {@code department} field in {@link Course}.
     * All cascade operations are propagated to the courses.
     * Lazily fetched to avoid unnecessary joins.
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

}
