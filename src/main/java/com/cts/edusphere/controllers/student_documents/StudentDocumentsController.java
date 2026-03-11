package com.cts.edusphere.controllers.student_documents;
import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.storage.StorageService;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.services.student_document.StudentDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        private final StorageService storageService;

    @PostMapping("/me/upload")
    public ResponseEntity<StudentDocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("docType") String docType) {

        StudentDocumentResponse response = studentDocumentService.uploadDocument(file, principal.userId(), docType);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDocumentResponse> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(studentDocumentService.getDocumentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentDocumentResponse>> getDocumentsByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentDocumentService.getAllDocumentsByStudentId(studentId));
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<Void> verifyDocument(@PathVariable UUID id, @RequestParam("status") boolean status) {
        studentDocumentService.verifyDocument(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        studentDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/docs")
    public ResponseEntity<List<StudentDocumentResponse>> getMyDocuments(
            @AuthenticationPrincipal UserPrincipal principal) {

        log.info("Fetching documents for logged-in user: {}", principal.userId());

        List<StudentDocumentResponse> responses = studentDocumentService
                .getAllDocumentsByStudentId(principal.userId());

        return ResponseEntity.ok(responses);
    }
}
