package com.cts.edusphere.modules.student_document;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.modules.student.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing a document uploaded by or on behalf of a student in
 * the EduSphere system.
 *
 * <p>A {@code StudentDocument} links a document file (identified by a URI) to a
 * specific {@link Student}, classifies the document by its {@link DocType}, and
 * tracks whether it has been verified by an authorised administrator.</p>
 *
 * <p>Records are persisted in the {@code student_document} table. An index on
 * {@code user_id} enables efficient retrieval of all documents belonging to a
 * given student.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID
 * primary key (mapped to {@code document_id}), optimistic-locking version, and
 * Spring Data JPA auditing timestamps.</p>
 *
 * @see Student
 * @see DocType
 */
@Entity
@Table(
        name = "student_document",
        indexes = {
                @Index(name = "idx_student_document_student", columnList = "user_id"),
        }
)
@AttributeOverride(name = "id", column = @Column(name = "document_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentDocument extends BaseEntity {

    /**
     * The student who owns this document.
     * Lazily fetched; the join column {@code user_id} is non-nullable.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student studentUser;

    /**
     * The category or type of the document (e.g., TRANSCRIPT, ID_PROOF, CERTIFICATE).
     * Stored as a string in the {@code doc_type} column; must not be {@code null}.
     */
    @Column(nullable = false, name = "doc_type")
    @Enumerated(EnumType.STRING)
    private DocType docType;

    /**
     * The URI pointing to the stored document file (e.g., an S3 path or a
     * server-relative file path). Must not be {@code null}.
     */
    @Column(nullable = false, name = "file_uri")
    private String fileUri;

    /**
     * Flag indicating whether the document has been reviewed and verified by
     * an authorised administrator. {@code false} by default until explicitly
     * approved.
     */
    @Column(nullable = false, name = "verification_status")
    private boolean verificationStatus;


}
