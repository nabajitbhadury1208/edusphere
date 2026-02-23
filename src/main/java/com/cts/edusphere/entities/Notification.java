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
    @Column(name = "notification_id", nullable = false, updatable = false)
    private UUID notificationId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // FK -> User

    // Generic "EntityID" from the diagram; kept scalar
    @Column(name = "entity_id")
    private UUID entityId;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Column(name = "category")
    private String category;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();
}