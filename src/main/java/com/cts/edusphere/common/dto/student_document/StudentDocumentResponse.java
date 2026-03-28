package com.cts.edusphere.common.dto.student_document;

import com.cts.edusphere.enums.DocType;

import java.util.UUID;

/**
 * Data Transfer Object representing a student document record returned from the API.
 *
 * @param studentDocumentId  the unique identifier of this student document record
 * @param studentId          the UUID of the student who owns this document
 * @param studentName        the display name of the student associated with this document
 * @param docType            the category of document (e.g., IDPROOF, TRANSCRIPT, CERTIFICATE)
 * @param downloadUrl        the URL at which the document file can be downloaded or accessed
 * @param verificationStatus {@code true} if the document has been verified by an authorised officer; {@code false} otherwise
 */
public record StudentDocumentResponse(
        UUID studentDocumentId,
        UUID studentId,
        String studentName,
        DocType docType,
        String downloadUrl,
        boolean verificationStatus
) {
}
