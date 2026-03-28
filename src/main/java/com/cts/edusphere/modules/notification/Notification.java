package com.cts.edusphere.modules.notification;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.NotificationType;
import com.cts.edusphere.modules.user.User;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity representing an in-application notification delivered to a user
 * in the EduSphere system.
 *
 * <p>A {@code Notification} is addressed to a specific {@link User} and carries
 * a text message categorised by a {@link NotificationType}. It may optionally
 * reference a domain entity (via {@link #entityId}) that the notification is
 * about. The {@link #isRead} flag tracks whether the recipient has acknowledged
 * the notification.</p>
 *
 * <p>Records are persisted in the {@code notifications} table. An index on
 * {@code user_id} supports efficient retrieval of all notifications for a
 * given user.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID
 * primary key (mapped to {@code notification_id}), optimistic-locking version,
 * and Spring Data JPA auditing timestamps.</p>
 *
 * @see NotificationType
 * @see User
 */
@Entity
@Table(
  name = "notifications",
  indexes = { @Index(name = "idx_notification_user", columnList = "user_id") }
)
@AttributeOverride(name = "id", column = @Column(name = "notification_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends BaseEntity {

  /**
   * The user to whom this notification is addressed.
   * Lazily fetched; the join column {@code user_id} is non-nullable.
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // TODO: Check this
  /**
   * UUID of the domain entity this notification is related to (e.g., an exam,
   * a grade, or a document). This field is optional and may be {@code null}
   * when the notification is not tied to a specific entity instance.
   */
  @Column(name = "entity_id")
  private UUID entityId;

  /**
   * The human-readable notification message presented to the user.
   * Limited to 2 000 characters; must not be {@code null}.
   */
  @Column(nullable = false, length = 2000, name = "message")
  private String message;

  /**
   * The category of this notification, used for filtering and display purposes
   * (e.g., ACADEMIC, SYSTEM, ALERT).
   * Stored as a string in the {@code category} column; must not be {@code null}.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private NotificationType category;

  /**
   * Flag indicating whether the recipient has read (acknowledged) this
   * notification. Defaults to {@code false} on creation.
   */
  @Column(nullable = false, name = "is_read")
  private boolean isRead;
}
