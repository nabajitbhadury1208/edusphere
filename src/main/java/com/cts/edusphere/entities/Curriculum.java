package com.cts.edusphere.entities;

import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name="curriculum",
        indexes = {
                @Index(name = "idx_curriculum_course", columnList = "course_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "curriculum_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Curriculum extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String description;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "modules_json", columnDefinition = "json")
    private String modulesJSON;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
