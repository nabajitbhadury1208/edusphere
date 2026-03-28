package com.cts.edusphere.modules.curriculum;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.courses.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing the curriculum associated with a specific course in
 * the EduSphere system.
 *
 * <p>A {@code Curriculum} belongs to exactly one {@link Course} and captures the
 * structured content of that course: a human-readable description and a JSON
 * representation of the individual teaching modules. The lifecycle of the curriculum
 * is tracked via a {@link Status} flag.</p>
 *
 * <p>Records are persisted in the {@code curriculum} table. An index on
 * {@code course_id} enables efficient retrieval of the curriculum for any given
 * course.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID
 * primary key (mapped to {@code curriculum_id}), optimistic-locking version, and
 * Spring Data JPA auditing timestamps.</p>
 *
 * @see Course
 * @see Status
 */
@Entity
@Table(
        name="curriculum",
        indexes = {
                @Index(name = "idx_curriculum_course", columnList = "course_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "curriculum_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Curriculum extends BaseEntity {

    /**
     * The course to which this curriculum belongs.
     * Lazily fetched; the join column {@code course_id} is non-nullable.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * A human-readable overview or summary of the curriculum content.
     * Must not be {@code null}.
     */
    @Column(nullable = false)
    private String description;

    /**
     * A JSON string representing the ordered list of teaching modules
     * included in this curriculum. Stored in a native JSON column
     * ({@code modules_json}) using Hibernate's JSON type mapping.
     * May be {@code null} if no structured module data has been entered yet.
     */
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "modules_json", columnDefinition = "json")
    private String modulesJSON;

    /**
     * The current lifecycle status of this curriculum (e.g., ACTIVE, DRAFT, ARCHIVED).
     * Stored as a string; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
