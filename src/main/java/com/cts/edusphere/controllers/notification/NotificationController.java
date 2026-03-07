package com.cts.edusphere.controllers.notification;

import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.services.notification.NotificationService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/notifcations")
public class NotificationController {

  private NotificationService notificationService;

  @PostMapping
  public ResponseEntity<String> createNotification(
    @Valid @RequestBody UUID userId,
    NotificationRequest notificationRequest
  ) {
    try {
      notificationService.createNotification(userId, notificationRequest);
      return ResponseEntity.ok("Successfully created user");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{userId}")
  public ResponseEntity<String> getAllNotificationsForUserId(
    @Valid @PathVariable UUID userId
  ) {
    try {
      notificationService.getAllNotificationsForUserId(userId);
      return ResponseEntity.ok(
        "Successfully fetched all notifications for user with id : " + userId
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PatchMapping("/{notificationId}/read")
  public ResponseEntity<String> markNotificationAsRead(
    @Valid @PathVariable UUID notificationId
  ) {
    try {
      notificationService.markNotificationAsRead(notificationId);
      return ResponseEntity.ok(
        "Successfully marked notification with id: " +
        notificationId +
        " as read"
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PatchMapping("/{userId}/read-all")
  public ResponseEntity<String> markAllNotificationsAsRead(
    @Valid @PathVariable UUID userId
  ) {
    try {
      notificationService.markAllNotificationsAsRead(userId);
      return ResponseEntity.ok(
        "Successfully marked all notification with user id: " +
        userId +
        " as read"
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/notifications/{notificationId}")
  public ResponseEntity<String> deleteNotificationById(
    @Valid @PathVariable UUID notificationId
  ) {
    try {
      notificationService.deleteNotificationById(notificationId);
      return ResponseEntity.ok(
        "Successfully deleted notification with id: " +
        notificationId +
        " as read"
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
