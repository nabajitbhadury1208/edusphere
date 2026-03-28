package com.cts.edusphere.services.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for student document management within EduSphere.
 *
 * <p>Provides methods for uploading, retrieving, verifying, downloading, and deleting
 * documents associated with student records. Documents are categorised by type (e.g.,
 * transcript, ID proof, certificate) and may require staff verification before they
 * are considered official.</p>
 */
public interface StudentDocumentService {

    /**
     * Uploads a new document file and associates it with the specified student.
     *
     * @param file      the {@link MultipartFile} to upload; must not be {@code null} or empty
     * @param studentId the {@link UUID} of the student to whom the document belongs
     * @param docType   a string identifying the document category (e.g., "TRANSCRIPT", "ID_PROOF")
     * @return a {@link StudentDocumentResponse} representing the stored document metadata
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     * @throws com.cts.edusphere.exceptions.FileStorageException       if the file cannot be stored (e.g., I/O error or invalid format)
     */
    StudentDocumentResponse uploadDocument(MultipartFile file, UUID studentId, String docType);

    /**
     * Retrieves the metadata for a single document by its unique identifier.
     *
     * @param id the {@link UUID} of the document to retrieve
     * @return a {@link StudentDocumentResponse} representing the found document
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no document exists with the given ID
     */
    StudentDocumentResponse getDocumentById(UUID id);

    /**
     * Retrieves all documents belonging to a specific student.
     *
     * @param studentId the {@link UUID} of the student whose documents are to be retrieved
     * @return a {@link List} of {@link StudentDocumentResponse} objects for the given student;
     *         never {@code null}, may be empty if the student has no documents
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    List<StudentDocumentResponse> getAllDocumentsByStudentId(UUID studentId);

    /**
     * Retrieves all documents of a specific type belonging to a given student.
     *
     * @param studentId the {@link UUID} of the student whose documents are to be filtered
     * @param docType   the document category to filter by (e.g., "TRANSCRIPT", "CERTIFICATE")
     * @return a {@link List} of {@link StudentDocumentResponse} objects matching the student and type;
     *         never {@code null}, may be empty if no matching documents exist
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    List<StudentDocumentResponse> getMyDocumentsByType(UUID studentId, String docType);

    /**
     * Retrieves all documents stored in the system across all students.
     *
     * @return a {@link List} of {@link StudentDocumentResponse} objects for every document;
     *         never {@code null}, may be empty
     */
    List<StudentDocumentResponse> getAllDocuments();

    /**
     * Updates the verification status of a document (e.g., approved or rejected by staff).
     *
     * @param id     the {@link UUID} of the document to verify
     * @param status {@code true} to mark the document as verified/approved;
     *               {@code false} to mark it as rejected
     * @return a {@link StudentDocumentResponse} reflecting the updated verification status
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no document exists with the given ID
     */
    StudentDocumentResponse verifyDocument(UUID id, boolean status);

    /**
     * Deletes the document identified by the given ID, removing both the metadata record
     * and the underlying stored file.
     *
     * @param id the {@link UUID} of the document to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no document exists with the given ID
     */
    void deleteDocument(UUID id);

    /**
     * Retrieves the stored file for a document as a Spring {@link Resource}, suitable for
     * streaming back to the client as a file download.
     *
     * @param id the {@link UUID} of the document whose file is to be downloaded
     * @return a {@link Resource} representing the file content
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no document exists with the given ID
     * @throws com.cts.edusphere.exceptions.FileStorageException       if the file cannot be read from the storage layer
     */
    Resource downloadDocument(UUID id);
}
