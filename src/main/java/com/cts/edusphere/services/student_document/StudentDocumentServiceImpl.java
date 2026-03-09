package com.cts.edusphere.services.student_document;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.storage.StorageService;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.StudentDocumentMapper;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.modules.StudentDocument;
import com.cts.edusphere.repositories.StudentDocumentRepository;
import com.cts.edusphere.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {
    private final StudentDocumentRepository studentDocumentRepository;
    private final StudentRepository studentRepository;
    private final StorageService storageService;
    private final StudentDocumentMapper studentDocumentMapper;

    @Override
    @Transactional
    public StudentDocumentResponse uploadDocument(MultipartFile file, UUID studentId, String docType) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + studentId)
        );

        DocType type = DocType.valueOf(docType.toUpperCase());
        String filePath = storageService.uploadFile(file);
        StudentDocument document = StudentDocument.builder()
                .student(student)
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
                () -> new ResourceNotFoundException("Student not found with id: " + studentId)
        );
        return studentDocumentRepository.findStudentDocumentById(studentId)
                .stream()
                .map(studentDocumentMapper::toResponse)
                .toList();
    }

    @Override
    public StudentDocumentResponse verifyDocument(UUID id, boolean status) {
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        document.setVerificationStatus(status);
        return studentDocumentMapper.toResponse(studentDocumentRepository.save(document));
    }

    @Override
    public void deleteDocument(UUID id) {
        if (!studentDocumentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document not found with id: " + id);
        }
        studentDocumentRepository.deleteById(id);
    }

    @Override
    public Resource downloadDocument(UUID id) {
        StudentDocument document = studentDocumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document not found with id: " + id)
        );
        return storageService.loadAsResource(document.getFileUri());
    }
}
