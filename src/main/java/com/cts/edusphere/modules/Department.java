package com.cts.edusphere.modules;

import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(
        name = "department",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_department_code", columnNames = "department_code")
        },
        indexes = @Index(name = "idx_department_head", columnList = "head_id")
)
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "department_id"))
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Department extends BaseEntity {
    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "head_id", nullable = true)
    private DepartmentHead head;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Faculty> faculties;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

}
