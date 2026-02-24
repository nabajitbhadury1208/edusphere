package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "thesis")
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

    @Column(nullable = false, name = "supervisor_id")
    private UUID supervisorId;

    @CreatedDate
    @Column(nullable = false, name = "submission_date")
    private Instant submissionDate;

    @Column(nullable = false, name = "status")
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