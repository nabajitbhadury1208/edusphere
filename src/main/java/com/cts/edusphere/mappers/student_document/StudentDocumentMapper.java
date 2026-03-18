package com.cts.edusphere.mappers.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.student_document.StudentDocument;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class StudentDocumentMapper {

    public StudentDocumentResponse toResponse(StudentDocument entity) {
        if (entity == null) return null;
        User user = entity.getStudentUser();
        String downloadUrl = "/api/v1/student-documents/download/" + entity.getId();
        return new StudentDocumentResponse(
                entity.getId(),
                user!= null ? user.getId() : null,
                user != null ? user.getName() : null,
                entity.getDocType(),
                downloadUrl,
                entity.isVerificationStatus()
        );
    }
}