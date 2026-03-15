package com.cts.edusphere.mappers.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentRequest;
import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.student_document.StudentDocument;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class StudentDocumentMapper {

    public StudentDocument toEntity(StudentDocumentRequest request, Student studentUser, String fileUri) {
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
        User user = entity.getStudentUser();
        return new StudentDocumentResponse(
                entity.getId(),
                user!= null ? user.getId() : null,
                user != null ? user.getName() : null,
                entity.getDocType(),
                entity.getFileUri(),
                entity.isVerificationStatus()
        );
    }
}