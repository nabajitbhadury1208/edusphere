package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record NotificationResponse(
  UUID notificationId,
  UUID userId,
  UUID entityId,
  String message,
  NotificationType category,
  Boolean isRead
) {}
