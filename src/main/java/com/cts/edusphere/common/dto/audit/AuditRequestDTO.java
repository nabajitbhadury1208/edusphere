package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.AuditStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

/**
 * Data Transfer Object for creating or updating an audit record.
 * Used as the request payload for audit operations.
 *
 * @param officerId  the UUID of the audit officer responsible for the audit; required on update
 * @param entityType the type of entity being audited (e.g., STUDENT, DEPARTMENT); required on create
 * @param entityId   the UUID of the specific entity instance being audited
 * @param scope      a brief description of what is covered by this audit; required on create
 * @param findings   narrative of observations or issues discovered during the audit
 * @param status     the current processing status of the audit
 */
public record AuditRequestDTO(
        @NotNull(groups = OnUpdate.class, message = "Officer ID is required")
        UUID officerId,

        @NotNull(groups = OnCreate.class, message = "Entity type is required")
        AuditEntityType entityType,

        UUID entityId,

        @NotBlank(groups = OnCreate.class, message = "Scope cannot be blank")
        String scope,

        String findings,

        AuditStatus status
) {}