package com.cts.edusphere.services.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.storage.StorageService;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.StudentDocumentMapper;
import com.cts.edusphere.modules.StudentDocument;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.StudentDocumentRepository;
import com.cts.edusphere.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {
    private final StudentDocumentRepository studentDocumentRepository;
    private final UserRepository studentRepository;
    private final StorageService storageService;
    private final StudentDocumentMapper studentDocumentMapper;

    @Override
    @Transactional
    public StudentDocumentResponse uploadDocument(MultipartFile file, UUID studentId, String docType) {
        User studentUser = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("student not found with id: " + studentId)
        );

        DocType type;
        try{
            type = DocType.valueOf(docType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid document type: " + String.join(", ", getDocTypeNames()));
        }
        String studentFolderPath = "students/" + studentId + "/";

        String filePath = storageService.uploadFile(file, studentFolderPath);
        StudentDocument document = StudentDocument.builder()
                .studentUser(studentUser)
                .docType(type)
                .fileUri(filePath)
                .verificationStatus(false)
                .build();
        return studentDocumentMapper.toResponse(studentDocumentRepository.save(document));
    }

    @Override
    public StudentDocumentResponse getDocumentById(UUID id) {
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        return studentDocumentMapper.toResponse(document);
    }

    @Override
    public List<StudentDocumentResponse> getAllDocumentsByStudentId(UUID studentId) {
        studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("student not found with id: " + studentId)
        );
        return studentDocumentRepository.findByStudentUserId(studentId)
                .stream()
                .map(studentDocumentMapper::toResponse)
                .toList();
    }

    @Override
    public List<StudentDocumentResponse> getMyDocumentsByType(UUID studentId, String docType) {
        studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("student not found with id: " + studentId)
        );
        DocType type;
        try{
            type = DocType.valueOf(docType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid document type: " + String.join(", ", getDocTypeNames()));
        }
        return studentDocumentRepository.findByStudentUserIdAndDocType(studentId, type)
                .stream()
                .map(studentDocumentMapper::toResponse)
                .toList();
    }

    @Override
    public List<StudentDocumentResponse> getAllDocuments() {
        return studentDocumentRepository.findAllOrderedByCreatedAt().stream().map(studentDocumentMapper ::toResponse).toList();
    }


    @Override
    @Transactional
    public StudentDocumentResponse verifyDocument(UUID id, boolean status) {
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        document.setVerificationStatus(status);
        return studentDocumentMapper.toResponse(studentDocumentRepository.save(document));
    }

    @Override
    @Transactional
    public void deleteDocument(UUID id) {
        if (!studentDocumentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document not found with id: " + id);
        }
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        storageService.deleteFile(document.getFileUri());
        studentDocumentRepository.deleteById(id);
    }

    @Override
    public Resource downloadDocument(UUID id) {
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        return storageService.loadAsResource(document.getFileUri());
    }

    private List<String> getDocTypeNames() {
        List<String> names = new ArrayList<>();
        for (DocType t : DocType.values()){
            names.add(t.name());
        }
        return names;
    }
}
