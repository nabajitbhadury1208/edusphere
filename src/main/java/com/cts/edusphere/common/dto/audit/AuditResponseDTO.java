package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.enums.AuditStatus;
import java.time.LocalDate;
import java.util.UUID;

public record AuditResponseDTO(UUID auditId, UUID officerId, String scope, String findings, LocalDate auditDate,
        AuditStatus status) {
}
