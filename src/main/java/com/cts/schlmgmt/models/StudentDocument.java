package com.cts.schlmgmt.models;

import com.cts.schlmgmt.enums.DocType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "STUDENT DOCUMENT")
public class StudentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "DocumentID")
    private UUID documentId;

    @Column(name = "StudentId")
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "Student")
    private Student student;

    @Column(name = "DocType")
    @Enumerated(EnumType.STRING)
    @NotNull
    private DocType doctype;

    @Column(name = "FileURI")
    @NotNull
    private String fileURI;

    @Column(name = "UploadedDate")
    @NotNull
    private LocalDateTime uploadDate;

    @Column(name = "VerificationStatus")
    @NotNull
    private boolean verificationStatus;


}
