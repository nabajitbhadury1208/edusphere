package com.cts.edusphere.services.notification;

import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.enums.NotificationType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.NoNotificationFoundWithId;
import com.cts.edusphere.exceptions.genericexceptions.NotificationNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.modules.notification.Notification;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.notification.NotificationRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private UUID userId;
    private UUID notificationId;
    private User user;
    private Notification notification;
    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        notificationId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("John Doe");

        notification = Notification.builder()
                .id(notificationId)
                .user(user)
                .entityId(UUID.randomUUID())
                .message("Test Message")
                .category(NotificationType.EXAM)
                .isRead(false)
                .build();

        notificationRequest = new NotificationRequest(
                userId,
                UUID.randomUUID(),
                "Test Message",
                NotificationType.EXAM,
                false
        );
    }

    @Test
    void testCreateNotification_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse result = notificationService.createNotification(userId, notificationRequest);

        assertNotNull(result);
        assertEquals("Test Message", result.message());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class, () -> 
            notificationService.createNotification(userId, notificationRequest));
        
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetAllNotificationsForUserId_Success() {
        when(notificationRepository.findByUser_Id(userId)).thenReturn(List.of(notification));

        List<NotificationResponse> result = notificationService.getAllNotificationsForUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).userId());
    }

    @Test
    void testGetAllNotificationsForUserId_Empty_ThrowsException() {
        when(notificationRepository.findByUser_Id(userId)).thenReturn(List.of());

        // Note: Your service catches the custom exception and wraps it in InternalServerErrorException
        assertThrows(InternalServerErrorException.class, () -> 
            notificationService.getAllNotificationsForUserId(userId));
    }

    @Test
    void testMarkNotificationAsRead_Success() {
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        assertDoesNotThrow(() -> notificationService.markNotificationAsRead(notificationId));
        
        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testMarkAllNotificationsAsRead_Success() {
        doNothing().when(notificationRepository).markAllAsReadByUserId(userId);

        assertDoesNotThrow(() -> notificationService.markAllNotificationsAsRead(userId));
        
        verify(notificationRepository, times(1)).markAllAsReadByUserId(userId);
    }

    @Test
    void testDeleteNotificationById_Success() {
        doNothing().when(notificationRepository).deleteById(notificationId);

        assertDoesNotThrow(() -> notificationService.deleteNotificationById(notificationId));
        
        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

    // @Test
    // void testSubscribeToNotifications_Success() {
    //     when(notificationRepository.findByUser_Id(userId)).thenReturn(List.of(notification));

    //     Flux<NotificationResponse> resultFlux = notificationService.subscribeToNofications(userId);

    //     StepVerifier.create(resultFlux)
    //             .expectNextMatches(resp -> resp.message().equals("Test Message"))
    //             .thenCancel() 
    //             .verify();
    // }
}