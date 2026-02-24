package com.cts.edusphere.entities;

import com.cts.edusphere.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@AttributeOverride(name = "id", column = @Column(name = "notification_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(nullable = false, length = 2000, name = "message")
    private String message;

    @Column(name = "category")
    private NotificationType category;

    @Column(nullable = false, name = "status")
    private boolean status;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}