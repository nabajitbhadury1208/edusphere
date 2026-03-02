package com.cts.edusphere.modules;

import com.cts.edusphere.enums.ThesisStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(
        name = "thesis",
        indexes = {
                @Index(name = "idx_thesis_student", columnList = "student_id"),
                @Index(name = "idx_thesis_supervisor", columnList = "supervisor_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "thesis_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Thesis extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, name = "title")
    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private Faculty supervisor;

    @Column(nullable = false, name = "submission_date")
    private LocalDate submissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ThesisStatus status;

}