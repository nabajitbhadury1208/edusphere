package com.cts.edusphere.repositories.student_document;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.student_document.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, UUID> {
    @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId")
    List<StudentDocument> findByStudentUserId(@Param("studentId") UUID studentId);

    @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId AND sd.docType = :docType")
    List<StudentDocument> findByStudentUserIdAndDocType(
            @Param("studentId") UUID studentId,
            @Param("docType") DocType docType);

    @Query("SELECT sd FROM StudentDocument sd ORDER BY sd.createdAt DESC")
    List<StudentDocument> findAllOrderedByCreatedAt();
}

