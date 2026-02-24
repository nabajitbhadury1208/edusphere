package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@AttributeOverride(name = "id", column = @Column(name = "report_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Report extends BaseEntity {

    @Column(nullable = false, name = "scope")
    private String scope;

    @Column(nullable = false, name = "metrics")
    private String metrics;

    @CreatedDate
    @Column(nullable = false, name = "generated_date")
    private Instant generatedDate;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
