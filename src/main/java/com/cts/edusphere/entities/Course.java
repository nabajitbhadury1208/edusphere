package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "course")
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder


public class Course extends BaseEntity{
    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private UUID departmentId;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private boolean status;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
