package com.cts.edusphere.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID auditId;

    @ManyToOne // One User may have many Audit Logs
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @NotNull
    private User user; // Foreign Key

    @Column
    @NotNull
    private String action;

    @Column
    @NotNull
    private String resource;

    @Column
    @NotNull
    private LocalDateTime timestamp;
}
