package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
  NotificationResponse createNotification(
    UUID userId,
    NotificationRequest notificationRequest
  );

  List<NotificationResponse> getAllNotificationsForUserId(UUID userId);

  void markNotificationAsRead(UUID notificationId);

  void markAllNotificationsAsRead(UUID userId);

  void deleteNotificationById(UUID notificationId);

  Flux<NotificationResponse> subscribeToNofications(UUID userId);
}
