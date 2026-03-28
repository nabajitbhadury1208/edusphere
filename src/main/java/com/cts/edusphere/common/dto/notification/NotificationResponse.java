package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import java.util.UUID;
import lombok.Builder;

/**
 * Data Transfer Object representing a notification record returned from the API.
 *
 * @param notificationId the unique identifier of this notification
 * @param userId         the UUID of the user who received this notification
 * @param entityId       the UUID of the related entity that triggered the notification
 * @param message        the notification content text
 * @param category       the type of notification (e.g., ENROLLMENT, EXAM, COMPLIANCE)
 * @param isRead         whether the notification has been read by the recipient
 */
@Builder
public record NotificationResponse(
  UUID notificationId,
  UUID userId,
  UUID entityId,
  String message,
  NotificationType category,
  Boolean isRead
) {}
