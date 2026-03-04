package com.cts.edusphere.common.dto.studentdocument;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.Student;

public record StudentDocumentResponse(
        Student student,
        DocType docType,
        String fileUri,
        boolean verificationStatus
) {
}
