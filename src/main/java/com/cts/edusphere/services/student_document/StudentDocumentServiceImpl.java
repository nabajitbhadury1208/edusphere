package com.cts.edusphere.services.student_document;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.storage.StorageService;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentDeletionFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentDownloadFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentUploadFailedException;
import com.cts.edusphere.exceptions.genericexceptions.StudentDocumentsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StudentNotFoundException;
import com.cts.edusphere.mappers.student_document.StudentDocumentMapper;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.student_document.StudentDocument;
import com.cts.edusphere.repositories.student.StudentRepository;
import com.cts.edusphere.repositories.student_document.StudentDocumentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service implementation for managing student document lifecycle within the EduSphere platform.
 *
 * <p>Handles upload, retrieval, filtering, verification, deletion, and download of
 * {@link com.cts.edusphere.modules.student_document.StudentDocument} entities. File storage is
 * delegated to a {@link com.cts.edusphere.common.storage.StorageService}, while database
 * persistence is managed through {@link com.cts.edusphere.repositories.student_document.StudentDocumentRepository}.
 * Compliance-sensitive operations are decorated with
 * {@link com.cts.edusphere.aspects.ComplianceAudit} to satisfy institutional audit requirements.</p>
 *
 * @see StudentDocumentService
 */
@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {
  private final StudentDocumentRepository studentDocumentRepository;
  private final StudentRepository studentRepository;
  private final StorageService storageService;
  private final StudentDocumentMapper studentDocumentMapper;

  /**
   * Uploads a document file for a specific student and persists the associated metadata.
   *
   * <p>Resolves the student by {@code studentId}, validates the {@code docType} string against
   * the {@link DocType} enum, uploads the file to the student-specific storage folder, and
   * saves a {@link com.cts.edusphere.modules.student_document.StudentDocument} record with
   * {@code verificationStatus} initially set to {@code false}.
   * This operation is subject to compliance auditing under
   * {@link AuditEntityType#STUDENT_DOCUMENT_CREATED}.</p>
   *
   * @param file      the multipart file to be stored; must not be {@code null} or empty
   * @param studentId the {@link UUID} of the student who owns the document; must not be
   *                  {@code null}
   * @param docType   a case-insensitive string matching one of the {@link DocType} enum
   *                  constants (e.g., {@code "TRANSCRIPT"})
   * @return a {@link StudentDocumentResponse} representing the persisted document record
   * @throws StudentNotFoundException              if no student exists for {@code studentId}
   * @throws IllegalArgumentException             if {@code docType} does not match any
   *                                              {@link DocType} constant
   * @throws StudentDocumentUploadFailedException if a domain-level upload failure is detected
   * @throws InternalServerErrorException         if any unexpected error occurs during upload
   */
  @Override
  @Transactional
  @ComplianceAudit(
      entityType = AuditEntityType.STUDENT_DOCUMENT_CREATED,
      scope = "Verify a new document that needs review")
  public StudentDocumentResponse uploadDocument(
      MultipartFile file, UUID studentId, String docType) {

    try {
      Student studentUser =
          studentRepository
              .findById(studentId)
              .orElseThrow(
                  () -> new StudentNotFoundException("student not found with id: " + studentId));

      DocType type;
      try {
        type = DocType.valueOf(docType.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Invalid document type: " + String.join(", ", getDocTypeNames()));
      }
      String studentFolderPath = "students/" + studentId + "/";

      String filePath = storageService.uploadFile(file, studentFolderPath);
      StudentDocument document =
          StudentDocument.builder()
              .studentUser(studentUser)
              .docType(type)
              .fileUri(filePath)
              .verificationStatus(false)
              .build();
      return studentDocumentMapper.toResponse(studentDocumentRepository.save(document));

    } catch (StudentDocumentUploadFailedException e) {
      throw new StudentDocumentUploadFailedException(
          "Failed to upload document: " + e.getMessage());

    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to upload document: " + e.getMessage());
    }
  }

  /**
   * Retrieves a single student document record by its unique identifier.
   *
   * @param id the {@link UUID} of the document to retrieve; must not be {@code null}
   * @return a {@link StudentDocumentResponse} containing the document metadata
   * @throws StudentDocumentNotFoundException if no document with the given {@code id} exists
   * @throws InternalServerErrorException     if an unexpected error occurs during retrieval
   */
  @Override
  public StudentDocumentResponse getDocumentById(UUID id) {
    try {
      StudentDocument document =
          studentDocumentRepository
              .findById(id)
              .orElseThrow(
                  () -> new StudentDocumentNotFoundException("Document not found with id: " + id));
      return studentDocumentMapper.toResponse(document);
    } catch (StudentDocumentNotFoundException e) {
      throw new StudentDocumentNotFoundException("Document not found with id: " + id);
    } catch (Exception e) {
      throw new InternalServerErrorException(
          "Failed to retrieve document details: " + e.getMessage());
    }
  }

  /**
   * Retrieves all document records belonging to a specific student.
   *
   * <p>First verifies that the student exists, then queries the repository for every document
   * associated with that student, ordered by creation time.</p>
   *
   * @param studentId the {@link UUID} of the student whose documents are to be retrieved;
   *                  must not be {@code null}
   * @return an unmodifiable {@link List} of {@link StudentDocumentResponse} objects (may be empty)
   * @throws StudentNotFoundException         if no student exists for {@code studentId}
   * @throws StudentDocumentNotFoundException if the repository signals that no documents exist
   *                                          for the student
   * @throws InternalServerErrorException     if an unexpected error occurs during retrieval
   */
  @Override
  public List<StudentDocumentResponse> getAllDocumentsByStudentId(UUID studentId) {
    try {
      studentRepository
          .findById(studentId)
          .orElseThrow(
              () -> new StudentNotFoundException("student not found with id: " + studentId));
      return studentDocumentRepository.findByStudentUserId(studentId).stream()
          .map(studentDocumentMapper::toResponse)
          .toList();

    } catch (StudentDocumentNotFoundException e) {
      throw new StudentDocumentNotFoundException(
          "No documents found for student with id: " + studentId);

    } catch (Exception e) {
      throw new InternalServerErrorException(
          "Failed to retrieve documents for student: " + e.getMessage());
    }
  }

  /**
   * Retrieves all document records for a specific student filtered by document type.
   *
   * <p>Verifies that the student exists, resolves the {@code docType} string to a {@link DocType}
   * enum constant, then queries the repository for matching documents.</p>
   *
   * @param studentId the {@link UUID} of the student whose documents are to be filtered;
   *                  must not be {@code null}
   * @param docType   a case-insensitive string matching one of the {@link DocType} enum
   *                  constants (e.g., {@code "TRANSCRIPT"})
   * @return an unmodifiable {@link List} of {@link StudentDocumentResponse} objects (may be empty)
   * @throws StudentNotFoundException         if no student exists for {@code studentId}
   * @throws IllegalArgumentException         if {@code docType} does not match any
   *                                          {@link DocType} constant
   * @throws StudentDocumentNotFoundException if the repository signals that no documents of the
   *                                          given type exist for the student
   * @throws InternalServerErrorException     if an unexpected error occurs during retrieval
   */
  @Override
  public List<StudentDocumentResponse> getMyDocumentsByType(UUID studentId, String docType) {
    try {
      studentRepository
          .findById(studentId)
          .orElseThrow(
              () -> new StudentNotFoundException("student not found with id: " + studentId));
      DocType type;

      try {
        type = DocType.valueOf(docType.toUpperCase());

      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Invalid document type: " + String.join(", ", getDocTypeNames()));
      }

      return studentDocumentRepository.findByStudentUserIdAndDocType(studentId, type).stream()
          .map(studentDocumentMapper::toResponse)
          .toList();
    } catch (StudentDocumentNotFoundException e) {
      throw new StudentDocumentNotFoundException(
          "No documents found for student with id: "
              + studentId
              + " and document type: "
              + docType);

    } catch (Exception e) {
      throw new InternalServerErrorException(
          "Failed to retrieve documents for student: " + e.getMessage());
    }
  }

  /**
   * Retrieves all student document records across all students, ordered by creation time.
   *
   * @return an unmodifiable {@link List} of {@link StudentDocumentResponse} objects (may be empty)
   * @throws StudentDocumentNotFoundException if the repository signals that no documents exist
   * @throws InternalServerErrorException     if an unexpected error occurs during retrieval
   */
  @Override
  public List<StudentDocumentResponse> getAllDocuments() {
    try {
      return studentDocumentRepository.findAllOrderedByCreatedAt().stream()
          .map(studentDocumentMapper::toResponse)
          .toList();
    } catch (StudentDocumentNotFoundException e) {
      throw new StudentDocumentNotFoundException("No documents found");

    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to retrieve documents: " + e.getMessage());
    }
  }

  /**
   * Updates the verification status of a student document.
   *
   * <p>Loads the document by {@code id}, sets its {@code verificationStatus} to the supplied
   * {@code status} boolean, and persists the change. This operation is subject to compliance
   * auditing under {@link AuditEntityType#STUDENT_DOCUMENT_APPROVAL}.</p>
   *
   * @param id     the {@link UUID} of the document to verify; must not be {@code null}
   * @param status {@code true} to mark the document as verified; {@code false} to revoke
   *               verification
   * @return a {@link StudentDocumentResponse} reflecting the updated verification status
   * @throws StudentDocumentNotFoundException if no document with the given {@code id} exists
   * @throws InternalServerErrorException     if an unexpected error occurs during the update
   */
  @Override
  @Transactional
  @ComplianceAudit(
      entityType = AuditEntityType.STUDENT_DOCUMENT_APPROVAL,
      scope = "Verify student docuemnt")
  public StudentDocumentResponse verifyDocument(UUID id, boolean status) {
    try {
      StudentDocument document =
          studentDocumentRepository
              .findById(id)
              .orElseThrow(
                  () -> new StudentDocumentNotFoundException("Document not found with id: " + id));
      document.setVerificationStatus(status);

      return studentDocumentMapper.toResponse(studentDocumentRepository.save(document));
    } catch (StudentDocumentNotFoundException e) {
      throw new StudentDocumentNotFoundException("Document not found with id: " + id);

    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to verify document: " + e.getMessage());
    }
  }

  /**
   * Deletes a student document record and its associated file from storage.
   *
   * <p>Verifies that the document exists, retrieves the stored file URI, delegates physical
   * file removal to the {@link com.cts.edusphere.common.storage.StorageService}, then removes
   * the database record. Operates within a transactional context.</p>
   *
   * @param id the {@link UUID} of the document to delete; must not be {@code null}
   * @throws StudentDocumentNotFoundException      if no document with the given {@code id} exists
   * @throws StudentDocumentDeletionFailedException if a domain-level deletion failure is detected
   * @throws InternalServerErrorException          if any unexpected error occurs during deletion
   */
  @Override
  @Transactional
  public void deleteDocument(UUID id) {
    try {
      if (!studentDocumentRepository.existsById(id)) {
        throw new StudentDocumentNotFoundException("Document not found with id: " + id);
      }
      StudentDocument document =
          studentDocumentRepository
              .findById(id)
              .orElseThrow(
                  () -> new StudentDocumentNotFoundException("Document not found with id: " + id));
      storageService.deleteFile(document.getFileUri());
      studentDocumentRepository.deleteById(id);
    } catch (StudentDocumentDeletionFailedException e) {
      throw new StudentDocumentDeletionFailedException("Failed to delete document with id: " + id);

    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to delete document: " + e.getMessage());
    }
  }

  /**
   * Downloads the file associated with a student document record.
   *
   * <p>Resolves the document by {@code id}, retrieves the stored file URI, and delegates to the
   * {@link com.cts.edusphere.common.storage.StorageService} to load the file as a
   * {@link org.springframework.core.io.Resource} suitable for streaming to the client.</p>
   *
   * @param id the {@link UUID} of the document to download; must not be {@code null}
   * @return a {@link Resource} pointing to the stored file content
   * @throws StudentDocumentNotFoundException       if no document with the given {@code id} exists
   * @throws StudentDocumentDownloadFailedException if a domain-level download failure is detected
   * @throws InternalServerErrorException           if any unexpected error occurs during download
   */
  @Override
  public Resource downloadDocument(UUID id) {
    try {
      StudentDocument document =
          studentDocumentRepository
              .findById(id)
              .orElseThrow(
                  () -> new StudentDocumentNotFoundException("Document not found with id: " + id));
      return storageService.loadAsResource(document.getFileUri());

    } catch (StudentDocumentDownloadFailedException e) {
      throw new StudentDocumentDownloadFailedException(
          "Failed to download document with id: " + id);

    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to download document: " + e.getMessage());
    }
  }

  /**
   * Returns the names of all valid {@link DocType} enum constants as a list of strings.
   *
   * <p>Used internally to compose human-readable error messages when an invalid document type
   * string is supplied by the caller.</p>
   *
   * @return a {@link List} of {@link String} values corresponding to each {@link DocType} name
   * @throws StudentDocumentsNotFoundException if the repository signals no document types exist
   * @throws InternalServerErrorException      if an unexpected error occurs while enumerating
   *                                           the constants
   */
  private List<String> getDocTypeNames() {
    try {
      List<String> names = new ArrayList<>();
      for (DocType t : DocType.values()) {
        names.add(t.name());
      }
      return names;

    } catch (StudentDocumentsNotFoundException e) {
      throw new StudentDocumentsNotFoundException("No document types found");

    } catch (Exception e) {
      throw new InternalServerErrorException(
          "Failed to retrieve document types: " + e.getMessage());
    }
  }
}
