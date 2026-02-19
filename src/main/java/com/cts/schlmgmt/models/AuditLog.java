package com.cts.schlmgmt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "AUDIT LOG")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditID")
    private UUID auditId;

    @ManyToOne // One User may have many Audit Logs
    @JoinColumn(table = "UserID")
    @NotNull
    private User User; // Foreign Key

    @Column(name = "Action")
    @NotNull
    private String action;

    @Column(name = "Resource")
    @NotNull
    private String resource;

    @Column(name = "Timestamp")
    @NotNull
    private LocalDateTime timestamp;
}
