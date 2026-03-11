package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.student_document.StudentDocumentRequest;
import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.modules.StudentDocument;
import com.cts.edusphere.modules.User;
import org.springframework.stereotype.Component;

@Component
public class StudentDocumentMapper {

    public StudentDocument toEntity(StudentDocumentRequest request, User studentUser, String fileUri) {
        if (request == null) return null;

        return StudentDocument.builder()
                .studentUser(studentUser)
                .docType(request.docType())
                .fileUri(fileUri)
                .verificationStatus(false)
                .build();
    }

    public StudentDocumentResponse toResponse(StudentDocument entity) {
        if (entity == null) return null;

        return new StudentDocumentResponse(
                entity.getId(),
                entity.getStudentUser(),
                entity.getDocType(),
                entity.getFileUri(),
                entity.isVerificationStatus()
        );
    }
}