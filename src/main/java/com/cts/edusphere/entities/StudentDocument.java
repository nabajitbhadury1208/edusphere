package com.cts.edusphere.entities;

import com.cts.edusphere.enums.DocType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_document")
@AttributeOverride(name = "id", column = @Column(name = "document_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentDocument extends BaseEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, name = "doc_type")
    @Enumerated(EnumType.STRING)
    private DocType docType;

    @Column(nullable = false, name = "file_uri")
    private String fileUri;

    @CreatedDate
    @Column(nullable = false, name = "upload_date")
    private Instant uploadDate;

    @Column(nullable = false, name = "verification_status")
    private boolean verificationStatus;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
