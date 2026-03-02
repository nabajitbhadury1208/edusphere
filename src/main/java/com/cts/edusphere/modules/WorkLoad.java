package com.cts.edusphere.modules;

import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, name = "hours")
    private Integer hours;

    @Column(nullable = false, name = "semester")
    private String semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}