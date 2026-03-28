package com.cts.edusphere.modules.thesis;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.student.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * JPA entity representing a thesis submission by a student in the EduSphere system.
 *
 * <p>A {@code Thesis} is authored by a single {@link Student}, supervised by a
 * designated {@link Faculty} member, and carries a title, a submission date, and a
 * lifecycle {@link ThesisStatus} (e.g., SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED).</p>
 *
 * <p>Records are persisted in the {@code thesis} table. Two indexes support efficient
 * retrieval by student ({@code student_id}) and by supervisor ({@code supervisor_id}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code thesis_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Student
 * @see Faculty
 * @see ThesisStatus
 */
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

    /**
     * The student who authored and submitted this thesis.
     * Lazily fetched; the join column {@code student_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * The title of the thesis. Must not be {@code null}.
     */
    @Column(nullable = false, name = "title")
    private String title;

    /**
     * The faculty member assigned as the supervisor for this thesis.
     * Lazily fetched; the join column {@code supervisor_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private Faculty supervisor;

    /**
     * The date on which the thesis was submitted by the student.
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "submission_date")
    private LocalDate submissionDate;

    /**
     * The current review or approval status of this thesis
     * (e.g., SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ThesisStatus status;

}
