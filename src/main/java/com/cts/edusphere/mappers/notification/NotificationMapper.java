package com.cts.edusphere.mappers.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.modules.notification.Notification;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

  public static Notification toEntity(
    NotificationRequest notificationRequest,
    User user
  ) {
    return Notification.builder()
      .user(user)
      .entityId(notificationRequest.entityId())
      .message(notificationRequest.message())
      .category(notificationRequest.category())
      .isRead(notificationRequest.isRead())
      .build();
  }

  public static NotificationResponse toDTO(Notification notification) {
    return NotificationResponse.builder()
      .notificationId(notification.getId())
      .userId(notification.getUser().getId())
      .entityId(notification.getEntityId())
      .message(notification.getMessage())
      .category(notification.getCategory())
      .isRead(notification.isRead())
      .build();
  }
}
