package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.NoNotificationFoundWithId;
import com.cts.edusphere.exceptions.genericexceptions.NoNotificationFoundWithUserId;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.mappers.NotificationMapper;
import com.cts.edusphere.modules.Notification;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.NotificationRepository;
import com.cts.edusphere.repositories.UserRepository;
import com.cts.edusphere.services.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  // private final UserServiceImpl us

  @Override
  public NotificationResponse createNotification(UUID userId, NotificationRequest notificationRequest) {
    try {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new UserNotFoundException("User with id {} not found " + userId));

      Notification notification = NotificationMapper.toEntity(notificationRequest, user);

      log.info("Created notification with id: " + notification.getId());

      return (NotificationMapper.toDTO(notificationRepository.save(notification)));
    } catch (Exception e) {
      log.error("Error creating notification");
      throw new InternalServerErrorException("Error creating notification");
    }
  }

  @Override
  public List<NotificationResponse> getAllNotificationsForUserId(UUID userId) {
    try {
      List<Notification> notifications = notificationRepository.findByUser_Id(userId);

      if (notifications.isEmpty()) {
        throw new NoNotificationFoundWithUserId("No notifications found with user id " + userId);
      }

      log.info("Successfully fetched notifications for user with id: " + userId);
      return notifications.stream().map(NotificationMapper::toDTO).toList();

    } catch (Exception e) {
      log.error("Error getting notifications for user with id {}", userId);
      throw new InternalServerErrorException("Error getting all notifications");
    }
  }

  @Override
  public void markNotificationAsRead(UUID notificationId) {
    try {
      Notification notification = notificationRepository.findById(notificationId)
          .orElseThrow(() -> new NoNotificationFoundWithId("No notification found with id " + notificationId));

      notification.setRead(true);

      notificationRepository.save(notification);

      log.info("Marked notification with id: {id} as read", notificationId);
    } catch (Exception e) {
      log.error("Error marking notification of id {}  as read", notificationId);
      throw new InternalServerErrorException("Error marking notification of id " + notificationId + " as read");
    }
  }

  @Override
  public void markAllNotificationsAsRead(UUID userId) {
    try {
      notificationRepository.markAllAsReadByUserId(userId);
      log.info("Marked all notifications of user with id {} as read", userId);
    } catch (Exception e) {
      log.error("Error getting all notifications");
      throw new InternalServerErrorException("Error getting all notifications");
    }
  }

  @Override
  public void deleteNotificationById(UUID notificationId) {
    try {
      notificationRepository.deleteById(notificationId);
      log.info("Deleted notification with id {}" + notificationId);
    } catch (Exception e) {
      log.error("Error deleting notifications of id: {}", notificationId);
      throw new InternalServerErrorException("Error deleting notifcation");
    }
  }
}
