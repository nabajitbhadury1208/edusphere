package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Data Transfer Object for sending a targeted notification to a specific user.
 *
 * @param userId   the UUID of the recipient user; must not be null
 * @param entityId the UUID of the related entity that triggered this notification; must not be null
 * @param message  the notification body text to be delivered to the user; must not be null
 * @param category the type of notification categorising its purpose (e.g., ENROLLMENT, EXAM, COMPLIANCE); must not be null
 * @param isRead   whether the notification has already been read by the recipient; must not be null
 */
public record NotificationRequest(
  @NotNull(message = "User must be present") UUID userId,

  @NotNull(message = "Entity id must be provided") UUID entityId,

  @NotNull(message = "Message must be provided") String message,

  @NotNull(message = "Category must be provided") NotificationType category,

  @NotNull(message = "Read status is needed") boolean isRead
) {}
