package com.cts.edusphere.common.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AuditRequestDTO(
        @NotNull(message = "Officer ID is required") UUID officerId,
        @NotNull(message = "Scope is required") String scope,
        @NotNull(message = "Findings are required") String findings,
        @NotNull(message = "Audit Date is required") String auditDate
) {}
