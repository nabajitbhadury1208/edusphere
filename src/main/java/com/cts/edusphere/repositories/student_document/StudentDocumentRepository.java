package com.cts.edusphere.repositories.student_document;

import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.student_document.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link StudentDocument} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom JPQL query methods for retrieving student documents filtered by student,
 * document type, and upload order.</p>
 */
@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, UUID> {

    /**
     * Retrieves all documents uploaded by a specific student.
     *
     * <p>Uses a JPQL query to match {@code studentUser.id} against the supplied student ID.</p>
     *
     * @param studentId the {@link UUID} of the student whose documents are to be fetched
     * @return a {@link List} of {@link StudentDocument} instances belonging to the given student;
     *         an empty list if no documents are found
     * @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId")
     */
    @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId")
    List<StudentDocument> findByStudentUserId(@Param("studentId") UUID studentId);

    /**
     * Retrieves all documents of a specific type uploaded by a specific student.
     *
     * <p>Uses a JPQL query to filter by both {@code studentUser.id} and {@code docType}.</p>
     *
     * @param studentId the {@link UUID} of the student whose documents are to be fetched
     * @param docType   the {@link DocType} enum value representing the category of document
     *                  (e.g., {@code TRANSCRIPT}, {@code ID_PROOF})
     * @return a {@link List} of {@link StudentDocument} instances matching both the student
     *         and document type; an empty list if no matches are found
     * @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId AND sd.docType = :docType")
     */
    @Query("SELECT sd FROM StudentDocument sd WHERE sd.studentUser.id = :studentId AND sd.docType = :docType")
    List<StudentDocument> findByStudentUserIdAndDocType(
            @Param("studentId") UUID studentId,
            @Param("docType") DocType docType);

    /**
     * Retrieves all student documents ordered by their creation timestamp in descending order.
     *
     * <p>Uses a JPQL query to return the most recently uploaded documents first.</p>
     *
     * @return a {@link List} of all {@link StudentDocument} instances sorted by
     *         {@code createdAt} descending; an empty list if no documents exist
     * @Query("SELECT sd FROM StudentDocument sd ORDER BY sd.createdAt DESC")
     */
    @Query("SELECT sd FROM StudentDocument sd ORDER BY sd.createdAt DESC")
    List<StudentDocument> findAllOrderedByCreatedAt();
}
