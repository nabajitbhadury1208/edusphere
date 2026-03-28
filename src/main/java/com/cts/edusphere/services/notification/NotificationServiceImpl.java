package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.BroadcastNotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.enums.Role;
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

/**
 * Implementation of {@link NotificationService} that handles all notification-related
 * business logic for the EduSphere platform.
 *
 * <p>This service supports both real-time reactive notification streaming via Project Reactor
 * and standard CRUD operations on persisted notifications. A multicast {@link Sinks.Many} sink
 * is used to push live notifications to any currently subscribed users.</p>
 *
 * <p>All public methods are transactional by default, as declared at the class level.</p>
 */
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

    /**
     * Opens a reactive stream that delivers both existing (persisted) notifications and any
     * new (live) notifications for the specified user.
     *
     * <p>The returned {@link Flux} first emits all notifications already stored in the database
     * for the user, then continues to emit new notifications as they arrive via the shared sink.</p>
     *
     * @param userId the {@link UUID} of the user whose notification stream is requested
     * @return a {@link Flux} of {@link NotificationResponse} objects; never {@code null}
     * @throws SubscribingToNotificationFailed if an expected subscription error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during stream setup
     */
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

    /**
     * Creates a new notification for a specific user and immediately emits it to the shared
     * reactive sink so that any active subscribers receive it in real time.
     *
     * @param userId              the {@link UUID} of the user who should receive the notification
     * @param notificationRequest a {@link NotificationRequest} DTO containing the notification
     *                            message and category details
     * @return the persisted {@link NotificationResponse} representing the created notification
     * @throws UserNotFoundException            if no user exists with the given {@code userId}
     * @throws NotificationNotCreatedException if a known creation error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during creation
     */
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

    /**
     * Broadcasts a notification to every user registered in the system.
     *
     * <p>Internally delegates to {@link #dispatchToUsers(List, BroadcastNotificationRequest)}
     * after fetching the full user list.</p>
     *
     * @param request a {@link BroadcastNotificationRequest} containing the message and category
     *                to broadcast
     * @return a {@link List} of {@link NotificationResponse} objects, one per user notified
     * @throws RuntimeException if any error occurs during broadcast
     */
    @Override
    public List<NotificationResponse> sendToAll(BroadcastNotificationRequest request) {
        try {
            List<User> allUsers = userRepository.findAll();
            return dispatchToUsers(allUsers, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Broadcasts a notification to all users who possess the specified role.
     *
     * <p>Internally delegates to {@link #dispatchToUsers(List, BroadcastNotificationRequest)}
     * after fetching users filtered by role.</p>
     *
     * @param role    the {@link Role} enum value used to filter target users
     * @param request a {@link BroadcastNotificationRequest} containing the message and category
     *                to broadcast
     * @return a {@link List} of {@link NotificationResponse} objects, one per matched user
     * @throws RuntimeException if any error occurs during role-based broadcast
     */
    @Override
    public List<NotificationResponse> sendToRole(Role role, BroadcastNotificationRequest request) {
        try{
            List<User> allUserWithRole = userRepository.findAllByRolesContaining(role);
            return dispatchToUsers(allUserWithRole, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Persists a notification for each user in the supplied list and emits every saved
     * notification through the reactive sink for real-time delivery to active subscribers.
     *
     * @param users   the {@link List} of {@link User} entities who should receive the notification
     * @param request a {@link BroadcastNotificationRequest} containing the message and category
     * @return a {@link List} of {@link NotificationResponse} objects representing all saved
     *         notifications
     */
    private List<NotificationResponse> dispatchToUsers(List<User> users, BroadcastNotificationRequest request) {
        var notifications = users.stream()
                .map(user ->
                        Notification.builder()
                        .user(user)
                        .message(request.message())
                        .category(request.category())
                        .isRead(false)
                        .build()
                ).toList();

        List<NotificationResponse> responses = notificationRepository.saveAll(notifications)
                .stream()
                .map(NotificationMapper::toDTO)
                .toList();

        responses.forEach(notificationSink::tryEmitNext);

        return responses;
    }

    /**
     * Retrieves all notifications associated with the specified user.
     *
     * @param userId the {@link UUID} of the user whose notifications are to be fetched
     * @return a non-empty {@link List} of {@link NotificationResponse} objects
     * @throws NotificationNotFoundException    if no notifications are found for the user
     * @throws NotificationsNotFoundException  if a known fetch error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during retrieval
     */
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

    /**
     * Marks a single notification as read by setting its {@code isRead} flag to {@code true}
     * and persisting the change.
     *
     * @param notificationId the {@link UUID} of the notification to mark as read
     * @throws NotificationNotFoundException   if no notification exists with the given ID
     * @throws NotificationNotUpdatedException if a known update error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during the update
     */
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

    /**
     * Marks all notifications belonging to the specified user as read in a single bulk operation.
     *
     * @param userId the {@link UUID} of the user whose notifications should all be marked as read
     * @throws NotificationNotUpdatedException if a known update error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during the bulk update
     */
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

    /**
     * Permanently deletes the notification identified by the given ID from the data store.
     *
     * @param notificationId the {@link UUID} of the notification to delete
     * @throws NotificationNotDeletedException if a known deletion error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during deletion
     */
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
