package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import jakarta.validation.constraints.NotNull;

public record BroadcastNotificationRequest(

        @NotNull(message = "Message must be provided") String message,

        @NotNull(message = "Category must be present") NotificationType category) {
}
