package com.cts.edusphere.modules.research_project;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.faculty.Faculty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing an academic research project in the EduSphere system.
 *
 * <p>A {@code ResearchProject} is led by a primary {@link Faculty} member and may
 * involve additional associated faculty and participating {@link Student}s. The
 * project has a defined title, start and end dates, and a lifecycle
 * {@link ProjectStatus}.</p>
 *
 * <p>The many-to-many relationship between projects and associated faculty members
 * is maintained through the {@code research_project_faculty} join table. The
 * many-to-many relationship with participating students uses the
 * {@code research_project_students} join table.</p>
 *
 * <p>Records are persisted in the {@code research_projects} table. An index on
 * {@code faculty_id} supports fast lookup of all projects led by a specific faculty
 * member.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code project_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Faculty
 * @see Student
 * @see ProjectStatus
 */
@Entity
@Table(
        name = "research_projects",
        indexes = {
                @Index(name = "idx_research_project_faculty", columnList = "faculty_id"),
        }
)
@AttributeOverride(name = "id", column = @Column(name = "project_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResearchProject extends BaseEntity {

    /**
     * The descriptive title of the research project.
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "title")
    private String title;

    /**
     * The faculty member who leads this research project.
     * Lazily fetched; excluded from JSON serialization to prevent circular references.
     * The join column {@code faculty_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnore
    private Faculty facultyLead;

    /**
     * The list of additional faculty members associated with this project.
     * Maintained through the {@code research_project_faculty} join table.
     * Lazily fetched; excluded from JSON serialization to prevent circular references.
     * Defaults to an empty list.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "research_project_faculty",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id")
    )
    @Builder.Default
    @JsonIgnore
    private List<Faculty> associatedFacultyMembers = new ArrayList<>();

    /**
     * The list of students participating in this research project.
     * Maintained through the {@code research_project_students} join table.
     * Lazily fetched; excluded from JSON serialization to prevent circular references.
     * Defaults to an empty list.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "research_project_students",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )

    @Builder.Default
    @JsonIgnore
    private List<Student> participatedStudents = new ArrayList<>();

    /**
     * The date on which the research project is scheduled to begin.
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    /**
     * The date on which the research project is scheduled to conclude.
     * Must not be {@code null}.
     */
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    /**
     * The current lifecycle status of this research project
     * (e.g., PROPOSED, ONGOING, COMPLETED, CANCELLED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;


}
