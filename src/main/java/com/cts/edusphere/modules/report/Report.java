package com.cts.edusphere.modules.report;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.modules.department.Department;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


/**
 * JPA entity representing a generated analytics or compliance report within the
 * EduSphere system.
 *
 * <p>A {@code Report} is produced by a designated {@link User} (the report generator),
 * may be scoped to a specific {@link Department}, and contains a JSON payload of
 * computed metrics. The scope of the report ({@link ReportScope}) determines the
 * breadth of data it covers (e.g., DEPARTMENT-level, INSTITUTION-wide). The lifecycle
 * is tracked by a {@link Status} flag.</p>
 *
 * <p>Records are persisted in the {@code report} table. Three indexes are defined to
 * support efficient filtering by the generating user ({@code generated_by_user_id}),
 * the associated department ({@code department_id}), and the scope ({@code scope}).</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code report_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see ReportScope
 * @see Status
 * @see Department
 */
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

    /**
     * The user who generated this report.
     * Lazily fetched; the join column {@code generated_by_user_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_user_id", nullable = false)
    private User generatedBy;

    /**
     * The department associated with this report.
     * Lazily fetched; the join column {@code department_id} is non-nullable.
     * May be used to scope the metrics to a single department when the
     * {@link #scope} is {@code DEPARTMENT}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * Defines the breadth of data covered by this report
     * (e.g., DEPARTMENT, INSTITUTION, COURSE).
     * Stored as a string in the {@code scope} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "scope")
    private ReportScope scope;

    /**
     * A JSON string containing the computed metrics payload for this report.
     * Stored in a native JSON column ({@code metrics}) using Hibernate's JSON
     * type mapping. Must not be {@code null}.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, name = "metrics", columnDefinition = "json")
    private String metrics;

    /**
     * The current processing or publication status of this report
     * (e.g., PENDING, COMPLETED, ARCHIVED).
     * Stored as a string in the {@code status} column; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private Status status;

}
