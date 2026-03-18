package com.cts.edusphere.common.dto.student_document;

import com.cts.edusphere.enums.DocType;

import java.util.UUID;

public record StudentDocumentResponse(
        UUID studentDocumentId,
        UUID studentId,
        String studentName,
        DocType docType,
        String downloadUrl,
        boolean verificationStatus
) {
}
