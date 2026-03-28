package com.cts.edusphere.mappers.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.student_document.StudentDocument;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting {@link StudentDocument} entity objects
 * into their corresponding DTO representation ({@link StudentDocumentResponse}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever student document
 * mapping is required. The download URL for each document is constructed dynamically
 * within this mapper using the entity's ID.</p>
 */
@Component
public class StudentDocumentMapper {

    /**
     * Converts a {@link StudentDocument} entity to a {@link StudentDocumentResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The associated user's ID and name are extracted safely; if no user is linked
     * to the document, both fields will be {@code null} in the response.
     * A download URL is constructed from the entity's ID using the standard
     * student-documents API path.</p>
     *
     * @param entity the {@link StudentDocument} entity to convert; may be {@code null}
     * @return a {@link StudentDocumentResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
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
