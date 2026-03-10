package com.cts.edusphere.common.dto.student_document;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.Student;
import jakarta.validation.constraints.NotNull;

public record StudentDocumentRequest(

        @NotNull(message = "student is required")
        Student student,

        @NotNull(message = "Document Type is required")
        DocType docType,

        @NotNull(message = "File URI is required")
        String file_uri,

        @NotNull(message = "Verification status is required")
        boolean verificationStatus
) {
}
