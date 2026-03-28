package com.cts.edusphere.modules.exam;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.courses.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * JPA entity representing a scheduled exam for a specific course in the EduSphere system.
 *
 * <p>An {@code Exam} is linked to a single {@link Course} and describes the examination
 * event: its type (e.g., MIDTERM, FINAL), the date on which it is scheduled, and its
 * current lifecycle {@link Status}.</p>
 *
 * <p>Records are persisted in the {@code exam} table. An index on {@code course_id}
 * enables efficient retrieval of all exams belonging to a given course.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code exam_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Course
 * @see ExamType
 * @see Status
 */
@Entity
@Table(
        name = "exam",
        indexes = {
                @Index(name = "idx_exam_course", columnList = "course_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "exam_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Exam extends BaseEntity {

    /**
     * The course for which this exam is scheduled.
     * Lazily fetched; the join column {@code course_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The type or category of the exam (e.g., MIDTERM, FINAL, QUIZ).
     * Stored as a string; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType type;

    /**
     * The scheduled date of the exam. Must not be {@code null}.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * The current lifecycle status of this exam (e.g., SCHEDULED, COMPLETED, CANCELLED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
