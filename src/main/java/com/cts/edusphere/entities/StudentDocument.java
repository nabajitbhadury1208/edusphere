package com.cts.edusphere.entities;

import com.cts.edusphere.enums.DocType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "student_document")
public class StudentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "student")
    private Student student;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocType doctype;

    @Column(nullable = false)
    private String fileURI;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    private boolean verificationStatus;


}
