package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "course",
        indexes = {
                @Index(name = "idx_course_department", columnList = "department_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder


public class Course extends BaseEntity{
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private boolean status;

}
