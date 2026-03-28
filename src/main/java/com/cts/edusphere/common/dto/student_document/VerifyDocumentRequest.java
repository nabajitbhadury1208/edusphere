package com.cts.edusphere.common.dto.student_document;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for updating the verification status of a student document.
 * Submitted by an authorised officer when approving or rejecting a document.
 *
 * @param verified {@code true} to mark the document as verified and approved;
 *                 {@code false} to reject or revoke verification
 */
public record VerifyDocumentRequest(
        @NotNull(message = "Verified status is required")
        boolean verified
) {
}
