package com.cts.edusphere.entities;

import com.cts.edusphere.enums.DocType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(
        name = "student_document",
        indexes = {
                @Index(name = "idx_student_document_student", columnList = "student_id"),
        }
)
@AttributeOverride(name = "id", column = @Column(name = "document_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentDocument extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, name = "doc_type")
    @Enumerated(EnumType.STRING)
    private DocType docType;

    @Column(nullable = false, name = "file_uri")
    private String fileUri;


    @Column(nullable = false, name = "verification_status")
    private boolean verificationStatus;


}
