package com.cts.edusphere.common.dto.audit;

import com.cts.edusphere.enums.AuditStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record AuditResponseDTO(
        @NotNull(message = "Audit ID is required") UUID auditId,
        @NotNull(message = "Officer ID is required") UUID officerId,
        @NotNull(message = "Scope is required") String scope,
        @NotNull(message = "Findings are required") String findings,
        @NotNull(message = "Audit Date is required") LocalDate auditDate,
        @NotNull(message = "Status is required") AuditStatus status) {
}
