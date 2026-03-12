package com.cts.edusphere.common.dto.student_document;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.modules.User;

import java.util.UUID;

public record StudentDocumentResponse(
        UUID studentDocumentId,
        UUID studentId,
        String studentName,
        DocType docType,
        String fileUri,
        boolean verificationStatus
) {
}
