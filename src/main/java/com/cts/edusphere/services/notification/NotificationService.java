package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.BroadcastNotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;

import com.cts.edusphere.enums.Role;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for notification management within EduSphere.
 *
 * <p>Supports targeted and broadcast notifications, read-state management, deletion,
 * and real-time reactive streaming of notifications to individual users via
 * Project Reactor's {@link Flux}.</p>
 */
public interface NotificationService {

  /**
   * Creates and delivers a notification to a specific user.
   *
   * @param userId              the {@link UUID} of the user who should receive the notification
   * @param notificationRequest the {@link NotificationRequest} containing the notification content; must not be {@code null}
   * @return a {@link NotificationResponse} representing the created notification
   * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
   */
  NotificationResponse createNotification(
    UUID userId,
    NotificationRequest notificationRequest
  );

  /**
   * Broadcasts a notification to every user registered in the system.
   *
   * @param request the {@link BroadcastNotificationRequest} containing the notification content; must not be {@code null}
   * @return a {@link List} of {@link NotificationResponse} objects, one per user notified;
   *         never {@code null}, may be empty if there are no users
   */
  List<NotificationResponse> sendToAll(BroadcastNotificationRequest request);

  /**
   * Broadcasts a notification to all users who hold a specific role.
   *
   * @param role    the {@link Role} identifying the group of users to notify (e.g., STUDENT, FACULTY, ADMIN)
   * @param request the {@link BroadcastNotificationRequest} containing the notification content; must not be {@code null}
   * @return a {@link List} of {@link NotificationResponse} objects, one per notified user;
   *         never {@code null}, may be empty if no users hold the given role
   */
  List<NotificationResponse> sendToRole(Role role, BroadcastNotificationRequest request);

  /**
   * Retrieves all notifications (read and unread) for a given user.
   *
   * @param userId the {@link UUID} of the user whose notifications are to be retrieved
   * @return a {@link List} of {@link NotificationResponse} objects belonging to the user;
   *         never {@code null}, may be empty
   * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
   */
  List<NotificationResponse> getAllNotificationsForUserId(UUID userId);

  /**
   * Marks a specific notification as read.
   *
   * @param notificationId the {@link UUID} of the notification to mark as read
   * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no notification exists with the given ID
   */
  void markNotificationAsRead(UUID notificationId);

  /**
   * Marks all notifications for a given user as read.
   *
   * @param userId the {@link UUID} of the user whose notifications are to be marked as read
   * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
   */
  void markAllNotificationsAsRead(UUID userId);

  /**
   * Deletes a specific notification by its unique identifier.
   *
   * @param notificationId the {@link UUID} of the notification to delete
   * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no notification exists with the given ID
   */
  void deleteNotificationById(UUID notificationId);

  /**
   * Returns a reactive stream of notifications for the specified user, enabling
   * real-time Server-Sent Events (SSE) or WebFlux-based push delivery.
   *
   * <p>The returned {@link Flux} emits {@link NotificationResponse} items as new
   * notifications are created for the user and completes when the subscription
   * is cancelled or the underlying sink is disposed.</p>
   *
   * @param userId the {@link UUID} of the user to subscribe to
   * @return a {@link Flux} that emits {@link NotificationResponse} items in real time
   */
  Flux<NotificationResponse> subscribeToNofications(UUID userId);
}
