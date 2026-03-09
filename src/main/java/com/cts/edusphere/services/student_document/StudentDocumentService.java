package com.cts.edusphere.services.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface StudentDocumentService {
    StudentDocumentResponse uploadDocument(MultipartFile file, UUID studentId, String docType);

    StudentDocumentResponse getDocumentById(UUID id);

    List<StudentDocumentResponse> getAllDocumentsByStudentId(UUID studentId);

    StudentDocumentResponse verifyDocument(UUID id, boolean status);

    void deleteDocument(UUID id);

    Resource downloadDocument(UUID id);
}
