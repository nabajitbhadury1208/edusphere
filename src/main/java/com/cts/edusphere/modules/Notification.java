package com.cts.edusphere.modules;

import com.cts.edusphere.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notification_user", columnList = "user_id")
        }
)
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


    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NotificationType category;

    @Column(nullable = false, name = "is_read")
    private boolean isRead;
}