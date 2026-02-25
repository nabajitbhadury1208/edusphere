package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.Instant;

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

    @Column(nullable = false, name = "generated_date")
    private Instant generatedDate;

}
