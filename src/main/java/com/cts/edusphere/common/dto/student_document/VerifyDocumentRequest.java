package com.cts.edusphere.common.dto.student_document;

import jakarta.validation.constraints.NotNull;

public record VerifyDocumentRequest(
        @NotNull(message = "Verified status is required")
        boolean verified
) {
}
