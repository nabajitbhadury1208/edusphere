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

@RestController
@RequestMapping("/api/v1/student-documents")
@RequiredArgsConstructor
@Slf4j
public class StudentDocumentsController {
    private final StudentDocumentService studentDocumentService;

    @PostMapping("/me/upload")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("docType") String docType) {

        StudentDocumentResponse response = studentDocumentService.uploadDocument(file, principal.userId(), docType);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER') or (hasRole('STUDENT') and @studentDocumentService.getDocumentById(#id)?.studentId() == principal.userId())")
    public ResponseEntity<StudentDocumentResponse> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(studentDocumentService.getDocumentById(id));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<StudentDocumentResponse>> getDocumentsByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentDocumentService.getAllDocumentsByStudentId(studentId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<List<StudentDocumentResponse>> getAllDocuments() {
        return ResponseEntity.ok(studentDocumentService.getAllDocuments());
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER')" + "or (hasRole('STUDENT') and @studentDocumentService.getDocumentById(#id)?.studentId() == principal.userId())")

    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        Resource file = studentDocumentService.downloadDocument(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('FACULTY', 'DEPARTMENT_HEAD', 'ADMIN')")
    public ResponseEntity<StudentDocumentResponse> verifyDocument(@PathVariable UUID id, @Valid @RequestBody VerifyDocumentRequest request) {
        StudentDocumentResponse updated = studentDocumentService.verifyDocument(id, request.verified());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        studentDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

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
