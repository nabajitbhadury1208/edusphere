package com.cts.edusphere.entities;

import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(
        name = "report",
        indexes = {
                @Index(name = "idx_report_generated_by", columnList = "generated_by_user_id"),
                @Index(name = "idx_report_department", columnList = "department_id"),
                @Index(name = "idx_report_scope", columnList = "scope")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "report_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Report extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_user_id", nullable = false)
    private User generatedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "scope")
    private ReportScope scope;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, name = "metrics", columnDefinition = "json")
    private String metrics;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private Status status;

}
