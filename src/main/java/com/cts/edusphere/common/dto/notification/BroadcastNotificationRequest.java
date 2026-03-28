package com.cts.edusphere.common.dto.notification;

import com.cts.edusphere.enums.NotificationType;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for sending a notification broadcast to all users.
 * Used by administrators to push system-wide announcements.
 *
 * @param message  the notification content to be delivered to all recipients; must not be null
 * @param category the type of notification categorising its purpose (e.g., ENROLLMENT, EXAM, COMPLIANCE)
 */
public record BroadcastNotificationRequest(

        @NotNull(message = "Message must be provided") String message,

        @NotNull(message = "Category must be present") NotificationType category) {
}
