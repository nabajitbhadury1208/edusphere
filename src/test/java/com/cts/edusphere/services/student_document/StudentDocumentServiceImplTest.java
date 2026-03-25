package com.cts.edusphere.services.student_document;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.storage.StorageService;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentNotFoundException;
import com.cts.edusphere.mappers.student_document.StudentDocumentMapper;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.student_document.StudentDocument;
import com.cts.edusphere.repositories.student.StudentRepository;
import com.cts.edusphere.repositories.student_document.StudentDocumentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class StudentDocumentServiceImplTest {

    @Mock
    private StudentDocumentRepository studentDocumentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private StudentDocumentMapper studentDocumentMapper;

    @InjectMocks
    private StudentDocumentServiceImpl studentDocumentService;

    private UUID studentId;
    private UUID documentId;
    private Student student;
    private StudentDocument document;
    private StudentDocumentResponse documentResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        documentId = UUID.randomUUID();

        student = Student.builder()
                .id(studentId)
                .name("John Doe")
                .build();

        document = StudentDocument.builder()
                .id(documentId)
                .studentUser(student)
                .docType(DocType.TRANSCRIPT)
                .fileUri("students/" + studentId + "/transcript.pdf")
                .verificationStatus(false)
                .build();

        documentResponse = new StudentDocumentResponse(
                documentId,
                studentId,
                "John Doe",
                DocType.TRANSCRIPT,
                "/api/v1/student-documents/download/" + documentId,
                false
        );
    }

    // ── uploadDocument ──────────────────────────────────────────────────

    @Test
    void uploadDocument_ShouldSucceed_WhenValidInput() {
        MultipartFile file = mock(MultipartFile.class);
        String docType = "TRANSCRIPT";

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(storageService.uploadFile(eq(file), any(String.class)))
                .thenReturn("students/" + studentId + "/transcript.pdf");
        when(studentDocumentRepository.save(any(StudentDocument.class))).thenReturn(document);
        when(studentDocumentMapper.toResponse(document)).thenReturn(documentResponse);

        StudentDocumentResponse result = studentDocumentService.uploadDocument(file, studentId, docType);

        assertNotNull(result);
        assertEquals(documentId, result.studentDocumentId());
        assertEquals(studentId, result.studentId());
        verify(studentDocumentRepository).save(any(StudentDocument.class));
        verify(storageService).uploadFile(eq(file), any(String.class));
    }

    @Test
    void uploadDocument_ShouldThrowInternalServerError_WhenStudentNotFound() {
        MultipartFile file = mock(MultipartFile.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.uploadDocument(file, studentId, "TRANSCRIPT"));
    }

    @Test
    void uploadDocument_ShouldThrowInternalServerError_WhenInvalidDocType() {
        MultipartFile file = mock(MultipartFile.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.uploadDocument(file, studentId, "INVALID_TYPE"));
    }

    // ── getDocumentById ─────────────────────────────────────────────────

    @Test
    void getDocumentById_ShouldReturnResponse_WhenDocumentExists() {
        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(studentDocumentMapper.toResponse(document)).thenReturn(documentResponse);

        StudentDocumentResponse result = studentDocumentService.getDocumentById(documentId);

        assertNotNull(result);
        assertEquals(documentId, result.studentDocumentId());
        verify(studentDocumentRepository).findById(documentId);
    }

    @Test
    void getDocumentById_ShouldThrowStudentDocumentNotFoundException_WhenNotFound() {
        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(StudentDocumentNotFoundException.class,
                () -> studentDocumentService.getDocumentById(documentId));
    }

    // ── getAllDocumentsByStudentId ───────────────────────────────────────

    @Test
    void getAllDocumentsByStudentId_ShouldReturnList_WhenStudentExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentDocumentRepository.findByStudentUserId(studentId)).thenReturn(List.of(document));
        when(studentDocumentMapper.toResponse(document)).thenReturn(documentResponse);

        List<StudentDocumentResponse> result = studentDocumentService.getAllDocumentsByStudentId(studentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(documentId, result.get(0).studentDocumentId());
        verify(studentDocumentRepository).findByStudentUserId(studentId);
    }

    @Test
    void getAllDocumentsByStudentId_ShouldReturnEmptyList_WhenNoDocuments() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentDocumentRepository.findByStudentUserId(studentId)).thenReturn(List.of());

        List<StudentDocumentResponse> result = studentDocumentService.getAllDocumentsByStudentId(studentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDocumentsByStudentId_ShouldThrowInternalServerError_WhenStudentNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.getAllDocumentsByStudentId(studentId));
    }

    // ── getMyDocumentsByType ────────────────────────────────────────────

    @Test
    void getMyDocumentsByType_ShouldReturnList_WhenValidInput() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentDocumentRepository.findByStudentUserIdAndDocType(studentId, DocType.TRANSCRIPT))
                .thenReturn(List.of(document));
        when(studentDocumentMapper.toResponse(document)).thenReturn(documentResponse);

        List<StudentDocumentResponse> result =
                studentDocumentService.getMyDocumentsByType(studentId, "TRANSCRIPT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DocType.TRANSCRIPT, result.get(0).docType());
    }

    @Test
    void getMyDocumentsByType_ShouldThrowInternalServerError_WhenStudentNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.getMyDocumentsByType(studentId, "TRANSCRIPT"));
    }

    @Test
    void getMyDocumentsByType_ShouldThrowInternalServerError_WhenInvalidDocType() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.getMyDocumentsByType(studentId, "INVALID_TYPE"));
    }

    // ── getAllDocuments ──────────────────────────────────────────────────

    @Test
    void getAllDocuments_ShouldReturnList_WhenDocumentsExist() {
        when(studentDocumentRepository.findAllOrderedByCreatedAt()).thenReturn(List.of(document));
        when(studentDocumentMapper.toResponse(document)).thenReturn(documentResponse);

        List<StudentDocumentResponse> result = studentDocumentService.getAllDocuments();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(studentDocumentRepository).findAllOrderedByCreatedAt();
    }

    @Test
    void getAllDocuments_ShouldReturnEmptyList_WhenNoDocuments() {
        when(studentDocumentRepository.findAllOrderedByCreatedAt()).thenReturn(List.of());

        List<StudentDocumentResponse> result = studentDocumentService.getAllDocuments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ── verifyDocument ──────────────────────────────────────────────────

    @Test
    void verifyDocument_ShouldUpdateStatus_WhenDocumentExists() {
        StudentDocumentResponse verifiedResponse = new StudentDocumentResponse(
                documentId, studentId, "John Doe", DocType.TRANSCRIPT,
                "/api/v1/student-documents/download/" + documentId, true
        );

        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(studentDocumentRepository.save(any(StudentDocument.class))).thenReturn(document);
        when(studentDocumentMapper.toResponse(document)).thenReturn(verifiedResponse);

        StudentDocumentResponse result = studentDocumentService.verifyDocument(documentId, true);

        assertNotNull(result);
        assertTrue(result.verificationStatus());
        verify(studentDocumentRepository).save(any(StudentDocument.class));
    }

    @Test
    void verifyDocument_ShouldThrowStudentDocumentNotFoundException_WhenNotFound() {
        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(StudentDocumentNotFoundException.class,
                () -> studentDocumentService.verifyDocument(documentId, true));
    }

    // ── deleteDocument ──────────────────────────────────────────────────

    @Test
    void deleteDocument_ShouldSucceed_WhenDocumentExists() {
        when(studentDocumentRepository.existsById(documentId)).thenReturn(true);
        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));
        doNothing().when(storageService).deleteFile(document.getFileUri());
        doNothing().when(studentDocumentRepository).deleteById(documentId);

        assertDoesNotThrow(() -> studentDocumentService.deleteDocument(documentId));

        verify(storageService).deleteFile(document.getFileUri());
        verify(studentDocumentRepository).deleteById(documentId);
    }

    @Test
    void deleteDocument_ShouldThrowInternalServerError_WhenDocumentNotFound() {
        when(studentDocumentRepository.existsById(documentId)).thenReturn(false);

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.deleteDocument(documentId));
    }

    // ── downloadDocument ────────────────────────────────────────────────

    @Test
    void downloadDocument_ShouldReturnResource_WhenDocumentExists() {
        Resource mockResource = mock(Resource.class);

        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(storageService.loadAsResource(document.getFileUri())).thenReturn(mockResource);

        Resource result = studentDocumentService.downloadDocument(documentId);

        assertNotNull(result);
        verify(storageService).loadAsResource(document.getFileUri());
    }

    @Test
    void downloadDocument_ShouldThrowInternalServerError_WhenDocumentNotFound() {
        when(studentDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class,
                () -> studentDocumentService.downloadDocument(documentId));
    }
}
