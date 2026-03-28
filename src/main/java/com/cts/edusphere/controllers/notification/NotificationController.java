package com.cts.edusphere.controllers.notification;

import com.cts.edusphere.common.dto.notification.BroadcastNotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.services.notification.NotificationService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing and delivering notifications.
 * Base path: /api/v1/notifications
 * All endpoints are restricted to ADMIN role.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Opens a Server-Sent Events (SSE) stream delivering real-time notifications to a specific user.
     * First emits all existing notifications for the user, then streams live notifications reactively.
     * Accessible by ADMIN role only.
     *
     * @param userId the UUID of the user to stream notifications for
     * @return a Flux of NotificationResponse objects as text/event-stream
     */
    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<NotificationResponse> streamNotifications(@PathVariable UUID userId) {
        return notificationService.subscribeToNofications(userId);
    }

    /**
     * Sends a targeted notification to a specific user.
     * Persists the notification and broadcasts it through the reactive sink.
     * Accessible by ADMIN role only.
     *
     * @param userId              the UUID of the recipient user
     * @param notificationRequest the request containing message and category
     * @return HTTP 201 with the created NotificationResponse
     */
    @PostMapping("/send/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> sendToUser(
            @PathVariable UUID userId,
            @Valid @RequestBody NotificationRequest notificationRequest) {
        NotificationResponse response = notificationService.createNotification(userId, notificationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Broadcasts a notification to all users in the system.
     * Creates individual notification records for every user and emits them via the reactive sink.
     * Accessible by ADMIN role only.
     *
     * @param request the broadcast request containing the message and notification category
     * @return HTTP 201 with a list of NotificationResponse objects for each recipient
     */
    @PostMapping("/send/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> sendToAll(
            @Valid @RequestBody BroadcastNotificationRequest request) {
        List<NotificationResponse> responses = notificationService.sendToAll(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    /**
     * Broadcasts a notification to all users with a specific role.
     * Accessible by ADMIN role only.
     *
     * @param role    the Role enum specifying the target audience (e.g., STUDENT, FACULTY)
     * @param request the broadcast request containing the message and category
     * @return HTTP 201 with a list of NotificationResponse objects for each matching recipient
     */
    @PostMapping("/send/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> sendToRole(
            @PathVariable Role role,
            @Valid @RequestBody BroadcastNotificationRequest request) {
        List<NotificationResponse> responses = notificationService.sendToRole(role, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    /**
     * Retrieves all notifications for a specific user.
     * Accessible by ADMIN role only.
     *
     * @param userId the UUID of the user whose notifications are to be retrieved
     * @return HTTP 200 with a list of NotificationResponse objects for the given user
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotificationsForUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getAllNotificationsForUserId(userId));
    }

    /**
     * Marks a single notification as read by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param notificationId the UUID of the notification to mark as read
     * @return HTTP 200 with a success confirmation message
     */
    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable UUID notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Successfully marked notification with id: " + notificationId + " as read");
    }

    /**
     * Marks all notifications for a specific user as read in a single batch operation.
     * Accessible by ADMIN role only.
     *
     * @param userId the UUID of the user whose notifications are all to be marked as read
     * @return HTTP 200 with a success confirmation message
     */
    @PatchMapping("/{userId}/read-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markAllNotificationsAsRead(@PathVariable UUID userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok("Successfully marked all notifications for user id: " + userId + " as read");
    }

    /**
     * Permanently deletes a notification by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param notificationId the UUID of the notification to delete
     * @return HTTP 200 with a success confirmation message
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNotificationById(@PathVariable UUID notificationId) {
        notificationService.deleteNotificationById(notificationId);
        return ResponseEntity.ok("Successfully deleted notification with id: " + notificationId);
    }
}