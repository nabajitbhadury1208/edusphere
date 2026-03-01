package com.cts.edusphere.modules;

import com.cts.edusphere.enums.GradeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @DecimalMax(value = "100.0", message = "Score cannot be greater than 100")
    @DecimalMin(value = "0.0", message = "Score cannot be less than 0")
    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GradeStatus status;

}