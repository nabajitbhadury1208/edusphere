package com.cts.edusphere.mappers.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.modules.notification.Notification;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Notification} entity objects
 * and their corresponding DTO representations ({@link NotificationRequest} and
 * {@link NotificationResponse}).
 *
 * <p>Both mapping methods are declared {@code static} and can be used without an instance.
 * This class is also a Spring-managed component for injection where needed.</p>
 */
@Component
public class NotificationMapper {

  /**
   * Converts a {@link NotificationRequest} DTO and a resolved {@link User} entity to
   * a {@link Notification} entity.
   *
   * <p>The {@link User} object is provided separately because the request DTO only carries
   * an identifier; the caller is responsible for resolving the user beforehand.</p>
   *
   * @param notificationRequest the {@link NotificationRequest} DTO containing notification data
   * @param user                the {@link User} entity to associate with the notification
   * @return a new {@link Notification} entity built from the request data and user
   */
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

  /**
   * Converts a {@link Notification} entity to a {@link NotificationResponse} DTO.
   *
   * <p>All fields, including the associated user's ID, are extracted from the entity
   * and mapped into the response DTO.</p>
   *
   * @param notification the {@link Notification} entity to convert
   * @return a {@link NotificationResponse} populated with data from the entity
   */
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
