package com.cts.edusphere.common.dto.student_document;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.Student;

import java.util.UUID;

public record StudentDocumentResponse(
        UUID studentDocumentId,
        Student student,
        DocType docType,
        String fileUri,
        boolean verificationStatus
) {
}
