package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record NotificationRequest(
  @NotNull(message = "User must be present") UUID userId,

  @NotNull(message = "Entity id must be provided") UUID entityId,

  @NotNull(message = "Message must be provided") String message,

  @NotNull(message = "Category must be provided") NotificationType category,

  @NotNull(message = "Read status is needed") boolean isRead
) {}
