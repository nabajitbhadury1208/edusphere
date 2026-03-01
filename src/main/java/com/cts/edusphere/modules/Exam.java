package com.cts.edusphere.modules;

import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType type;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
