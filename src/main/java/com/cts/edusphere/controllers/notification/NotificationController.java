package com.cts.edusphere.controllers.notification;

import com.cts.edusphere.common.dto.ApiResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.userId().toString()")
    public Flux<NotificationResponse> streamNotifications(@PathVariable UUID userId) {
        return notificationService.subscribeToNofications(userId);
    }

    @PostMapping("/send/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.userId().toString()")
    public ResponseEntity<NotificationResponse> sendToUser(
            @PathVariable UUID userId,
            @Valid @RequestBody NotificationRequest notificationRequest) {
        NotificationResponse response = notificationService.createNotification(userId, notificationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/send/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> sendToAll(
            @Valid @RequestBody BroadcastNotificationRequest request) {
        List<NotificationResponse> responses = notificationService.sendToAll(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    @PostMapping("/send/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> sendToRole(
            @PathVariable Role role,
            @Valid @RequestBody BroadcastNotificationRequest request) {
        List<NotificationResponse> responses = notificationService.sendToRole(role, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.userId().toString()")
    public ResponseEntity<List<NotificationResponse>> getAllNotificationsForUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getAllNotificationsForUserId(userId));
    }

    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> markNotificationAsRead(@PathVariable UUID notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.of("Successfully marked notification with id: " + notificationId + " as read", HttpStatus.OK.value()));
    }

    @PatchMapping("/{userId}/read-all")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.userId().toString()")
    public ResponseEntity<ApiResponse> markAllNotificationsAsRead(@PathVariable UUID userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok(ApiResponse.of("Successfully marked all notifications for user id: " + userId + " as read", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteNotificationById(@PathVariable UUID notificationId) {
        notificationService.deleteNotificationById(notificationId);
        return ResponseEntity.ok(ApiResponse.of("Successfully deleted notification with id: " + notificationId, HttpStatus.OK.value()));
    }
}