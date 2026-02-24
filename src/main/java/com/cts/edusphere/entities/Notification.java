package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID notificationId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user; // FK -> User

    // Generic "EntityID" from the diagram; kept scalar
    @Column
    private UUID entityId;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column
    private String category;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdDate = Instant.now();
}