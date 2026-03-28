package com.cts.edusphere.aspects;

import com.cts.edusphere.enums.AuditEntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a service method for automatic compliance audit record generation.
 *
 * <p>When a method annotated with {@code @ComplianceAudit} returns successfully,
 * the {@link com.cts.edusphere.aspects.AuditAutoGeneratorAspect} intercepts the
 * return value, extracts the entity ID via reflection, and persists a new
 * {@link com.cts.edusphere.modules.compliance_record.ComplianceRecord} with status
 * {@code PENDING} for review by a compliance officer.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * @ComplianceAudit(
 *     entityType = AuditEntityType.STUDENT_CREATED,
 *     scope = "Verify new student eligibility and documentation"
 * )
 * public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) { ... }
 * }</pre>
 *
 * @see com.cts.edusphere.aspects.AuditAutoGeneratorAspect
 * @see com.cts.edusphere.enums.AuditEntityType
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComplianceAudit {

    /**
     * The type of entity or event that triggered this compliance audit.
     * Determines how the audit record is categorised in the compliance dashboard.
     *
     * @return the {@link AuditEntityType} associated with this audit event
     */
    AuditEntityType entityType();

    /**
     * A human-readable description of what the compliance officer should verify
     * when reviewing the generated audit record.
     *
     * @return the audit scope description; defaults to {@code "System auto generated audit response"}
     */
    String scope() default "System auto generated audit response";
}
