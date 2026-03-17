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

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {
  private final StudentDocumentRepository studentDocumentRepository;
  private final StudentRepository studentRepository;
  private final StorageService storageService;
  private final StudentDocumentMapper studentDocumentMapper;

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
