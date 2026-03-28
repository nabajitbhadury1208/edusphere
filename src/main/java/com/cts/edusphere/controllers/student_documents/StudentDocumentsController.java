package com.cts.edusphere.controllers.student_documents;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.dto.student_document.VerifyDocumentRequest;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.services.student_document.StudentDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing student document uploads, retrieval, verification, and deletion.
 * Base path: /api/v1/student-documents
 */
@RestController
@RequestMapping("/api/v1/student-documents")
@RequiredArgsConstructor
@Slf4j
public class StudentDocumentsController {
    private final StudentDocumentService studentDocumentService;

    /**
     * Uploads a document for the authenticated student.
     * Validates docType, stores the file, and triggers a compliance audit.
     * Accessible by STUDENT role only.
     *
     * @param file the multipart file to upload
     * @param principal the authenticated student principal
     * @param docType the document type string
     * @return HTTP 201 with the created StudentDocumentResponse
     */
    @PostMapping("/me/upload")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("docType") String docType) {

        StudentDocumentResponse response = studentDocumentService.uploadDocument(file, principal.userId(), docType);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a document by its unique identifier.
     * Accessible by FACULTY, ADMIN, DEPARTMENT_HEAD, COMPLIANCE_OFFICER, or the owning student.
     *
     * @param id the UUID of the document to retrieve
     * @return HTTP 200 with the matching StudentDocumentResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER') or (hasRole('STUDENT') and @studentDocumentService.getDocumentById(#id)?.studentId() == principal.userId())")
    public ResponseEntity<StudentDocumentResponse> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(studentDocumentService.getDocumentById(id));
    }

    /**
     * Retrieves all documents for a specific student.
     * Accessible by FACULTY, ADMIN, DEPARTMENT_HEAD, and COMPLIANCE_OFFICER roles.
     *
     * @param studentId the UUID of the student whose documents to retrieve
     * @return HTTP 200 with a list of StudentDocumentResponse objects
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<StudentDocumentResponse>> getDocumentsByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentDocumentService.getAllDocumentsByStudentId(studentId));
    }

    /**
     * Retrieves all documents across all students.
     * Accessible by FACULTY, ADMIN, DEPARTMENT_HEAD, and COMPLIANCE_OFFICER roles.
     *
     * @return HTTP 200 with a list of all StudentDocumentResponse objects
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<StudentDocumentResponse>> getAllDocuments() {
        return ResponseEntity.ok(studentDocumentService.getAllDocuments());
    }

    /**
     * Downloads the physical file of a document as an attachment.
     * Accessible by FACULTY, ADMIN, DEPARTMENT_HEAD, COMPLIANCE_OFFICER, or the owning student.
     *
     * @param id the UUID of the document to download
     * @return HTTP 200 with the file as a Resource attachment
     */
    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')" + "or (hasRole('STUDENT') and @studentDocumentService.getDocumentById(#id)?.studentId() == principal.userId())")

    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        Resource file = studentDocumentService.downloadDocument(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Sets the verification status of a document and triggers a compliance audit.
     * Accessible by FACULTY, DEPARTMENT_HEAD, and ADMIN roles.
     *
     * @param id the UUID of the document to verify
     * @param request the verification request containing the verified status
     * @return HTTP 200 with the updated StudentDocumentResponse
     */
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('FACULTY', 'DEPARTMENT_HEAD', 'ADMIN')")
    public ResponseEntity<StudentDocumentResponse> verifyDocument(@PathVariable UUID id, @Valid @RequestBody VerifyDocumentRequest request) {
        StudentDocumentResponse updated = studentDocumentService.verifyDocument(id, request.verified());
        return ResponseEntity.ok(updated);
    }

    /**
     * Permanently deletes a document and its physical file.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the document to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        studentDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves documents of the authenticated student, optionally filtered by document type.
     * Accessible by STUDENT role only.
     *
     * @param principal the authenticated student principal
     * @param docType optional document type filter; returns all documents if not provided
     * @return HTTP 200 with a list of matching StudentDocumentResponse objects
     */
    @GetMapping("/me/docs")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<StudentDocumentResponse>> getMyDocuments(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(value = "docType", required = false) String docType
    ) {

        log.info("Fetching documents for logged-in user: {}", principal.userId());

        List<StudentDocumentResponse> responses;
        if (docType != null && !docType.isBlank()) {
            responses = studentDocumentService.getMyDocumentsByType(principal.userId(), docType);
        } else {
            responses = studentDocumentService.getAllDocumentsByStudentId(principal.userId());
        }

        return ResponseEntity.ok(responses);
    }
}
