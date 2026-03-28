package com.cts.edusphere.modules.grade;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.GradeStatus;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.exam.Exam;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing a student's grade for a specific exam in the EduSphere system.
 *
 * <p>A {@code Grade} record links a {@link Student} to an {@link Exam} and stores the
 * numeric score they achieved, the corresponding letter or categorical grade string,
 * and the processing {@link GradeStatus} (e.g., PENDING, PUBLISHED).</p>
 *
 * <p>Records are persisted in the {@code grades} table. Two indexes are defined to
 * support efficient queries: one by student ({@code student_id}) and one by exam
 * ({@code exam_id}).</p>
 *
 * <p>Score values are validated at the Bean Validation layer to fall within the range
 * [0.0, 100.0] inclusive.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code grade_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Student
 * @see Exam
 * @see GradeStatus
 */
@Entity
@Table(
        name = "grades",
        indexes = {
                @Index(name = "idx_grade_student", columnList = "student_id"),
                @Index(name = "idx_grade_exam", columnList = "exam_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "grade_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Grade extends BaseEntity {

    /**
     * The exam for which this grade was awarded.
     * Lazily fetched; the join column {@code exam_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    /**
     * The student who sat the exam and received this grade.
     * Lazily fetched; the join column {@code student_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * The numeric score achieved by the student, expressed as a percentage.
     * Must be between 0.0 and 100.0 inclusive (validated by Bean Validation
     * constraints). Stored in the {@code score} column; must not be {@code null}.
     */
    @DecimalMax(value = "100.0", message = "Score cannot be greater than 100")
    @DecimalMin(value = "0.0", message = "Score cannot be less than 0")
    @Column(name = "score", nullable = false)
    private Double score;

    /**
     * The letter or categorical grade assigned based on the {@link #score}
     * (e.g., "A", "B+", "Pass"). Must not be {@code null}.
     */
    @Column(name = "grade", nullable = false)
    private String grade;

    /**
     * The current processing status of this grade record
     * (e.g., PENDING, PUBLISHED, DISPUTED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GradeStatus status;

}
