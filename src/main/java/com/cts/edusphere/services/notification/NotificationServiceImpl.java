package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.exceptions.genericexceptions.NotificationNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.NotificationNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.NotificationNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.NotificationNotUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.NotificationsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.SubscribingToNotificationFailed;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.mappers.notification.NotificationMapper;
import com.cts.edusphere.modules.notification.Notification;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.notification.NotificationRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  // Many => Multiple messages; multicast() => multiple listeners;
  // onBackpressureBuffer() => for handling backpressure, it stores in buffer to
  // try later
  private final Sinks.Many<NotificationResponse> notificationSink =
      Sinks.many().multicast().onBackpressureBuffer();

  public Flux<NotificationResponse> subscribeToNofications(UUID userId) {
    try {
        Flux<NotificationResponse> existingNotifs =
            Flux.fromIterable(notificationRepository.findByUser_Id(userId))
                .map(NotificationMapper::toDTO);
    
        Flux<NotificationResponse> liveNotifs =
            notificationSink.asFlux().filter(notif -> notif.userId().equals(userId));
    
        return Flux.concat(existingNotifs, liveNotifs)
            .doOnSubscribe(s -> log.info("User {} subscribed", userId));
    } catch (SubscribingToNotificationFailed e) {
        log.error("Error subscribing to notifications for user with id {}", userId);
        throw new SubscribingToNotificationFailed("Error subscribing to notifications");
    } catch (Exception e) {
        log.error("Unexpected error subscribing to notifications for user with id {}: {}", userId, e.getMessage());
        throw new InternalServerErrorException("Unexpected error subscribing to notifications");
    }
  }

  @Override
  public NotificationResponse createNotification(
      UUID userId, NotificationRequest notificationRequest) {
    try {
      User user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new UserNotFoundException("User with id {} not found " + userId));

      Notification notification = NotificationMapper.toEntity(notificationRequest, user);
      NotificationResponse notificationResponse =
          NotificationMapper.toDTO(notificationRepository.save(notification));

      notificationSink.tryEmitNext(notificationResponse);

      log.info("Created notification and sent to id: {} ", notification.getId());

      return (notificationResponse);
    } catch (NotificationNotCreatedException e) {
      log.error("Error creating notification");
      throw new NotificationNotCreatedException("Error creating notification");
    } catch (Exception e) {
      log.error("Unexpected error creating notification for user with id {}: {}", userId, e.getMessage());
      throw new InternalServerErrorException("Unexpected error creating notification");
    }
  }

  @Override
  public List<NotificationResponse> getAllNotificationsForUserId(UUID userId) {
    try {
      List<Notification> notifications = notificationRepository.findByUser_Id(userId);

      if (notifications.isEmpty()) {
        throw new NotificationNotFoundException("No notifications found with user id " + userId);
      }

      log.info("Successfully fetched notifications for user with id: " + userId);
      return notifications.stream().map(NotificationMapper::toDTO).toList();

    } catch (NotificationsNotFoundException e) {
      log.error("Error getting notifications for user with id {}", userId);
      throw new NotificationsNotFoundException("Error getting all notifications");
    } catch (Exception e) {
      log.error("Unexpected error fetching notifications for user with id {}: {}", userId, e.getMessage());
      throw new InternalServerErrorException("Unexpected error fetching notifications");
    }
  }

  @Override
  public void markNotificationAsRead(UUID notificationId) {
    try {
      Notification notification =
          notificationRepository
              .findById(notificationId)
              .orElseThrow(
                  () ->
                      new NotificationNotFoundException(
                          "No notification found with id " + notificationId));

      notification.setRead(true);

      notificationRepository.save(notification);

      log.info("Marked notification with id: {} as read", notificationId);
    } catch (NotificationNotUpdatedException e) {
      log.error("Error marking notification of id {}  as read", notificationId);
      throw new NotificationNotUpdatedException(
          "Error marking notification of id " + notificationId + " as read");
    } catch (Exception e) {
      log.error("Unexpected error marking notification of id {} as read: {}", notificationId, e.getMessage());
      throw new InternalServerErrorException("Unexpected error marking notification as read");
    }
  }

  @Override
  public void markAllNotificationsAsRead(UUID userId) {
    try {
      notificationRepository.markAllAsReadByUserId(userId);
      log.info("Marked all notifications of user with id {} as read", userId);
    } catch (NotificationNotUpdatedException e) {
      log.error("Error marking all notifications as read");
      throw new NotificationNotUpdatedException("Error marking all notifications as read");
    } catch (Exception e) {
      log.error("Unexpected error marking all notifications of user with id {} as read: {}", userId, e.getMessage());
      throw new InternalServerErrorException("Unexpected error marking all notifications as read");
    }
  }

  @Override
  public void deleteNotificationById(UUID notificationId) {
    try {
      notificationRepository.deleteById(notificationId);
      log.info("Deleted notification with id {}" + notificationId);
    } catch (NotificationNotDeletedException e) {
      log.error("Error deleting notifications of id: {}", notificationId);
      throw new NotificationNotDeletedException("Error deleting notifcation");
    } catch (Exception e) {
      log.error("Unexpected error deleting notification with id {}: {}", notificationId, e.getMessage());
      throw new InternalServerErrorException("Unexpected error deleting notification");
    }
  }
}
