package com.cts.edusphere.controllers.notification;

import com.cts.edusphere.common.dto.notification.BroadcastNotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationRequest;
import com.cts.edusphere.common.dto.notification.NotificationResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.NotificationType;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.services.notification.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class NotificationControllerTest {

    @TestConfiguration
    static class TestJacksonConfig {
        @Bean
        ObjectMapper objectMapper() {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
        }
    }

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private com.cts.edusphere.services.audit_log.AuditLogService auditLogService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private UUID entityId;
    private UUID notificationId;
    private NotificationRequest notificationRequest;
    private NotificationResponse notificationResponse;
    private BroadcastNotificationRequest broadcastRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        entityId = UUID.randomUUID();
        notificationId = UUID.randomUUID();

        notificationRequest = new NotificationRequest(userId, entityId, "Test notification", NotificationType.COURSE, false);
        notificationResponse = NotificationResponse.builder()
                .notificationId(notificationId)
                .userId(userId)
                .entityId(entityId)
                .message("Test notification")
                .category(NotificationType.COURSE)
                .isRead(false)
                .build();
        broadcastRequest = new BroadcastNotificationRequest("Broadcast message", NotificationType.ENROLLMENT);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/notifications/send/user/{userId} - Returns 201 Created")
    void sendToUser_ReturnsCreated() throws Exception {
        when(notificationService.createNotification(eq(userId), any(NotificationRequest.class))).thenReturn(notificationResponse);

        mockMvc.perform(post("/api/v1/notifications/send/user/{userId}", userId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(notificationResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/notifications/send/all - Returns 201 Created")
    void sendToAll_ReturnsCreated() throws Exception {
        List<NotificationResponse> responses = List.of(notificationResponse);
        when(notificationService.sendToAll(any(BroadcastNotificationRequest.class))).thenReturn(responses);

        mockMvc.perform(post("/api/v1/notifications/send/all").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broadcastRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/notifications/send/role/{role} - Returns 201 Created")
    void sendToRole_ReturnsCreated() throws Exception {
        List<NotificationResponse> responses = List.of(notificationResponse);
        when(notificationService.sendToRole(eq(Role.STUDENT), any(BroadcastNotificationRequest.class))).thenReturn(responses);

        mockMvc.perform(post("/api/v1/notifications/send/role/{role}", Role.STUDENT).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broadcastRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/notifications/{userId} - Returns 200 OK with List")
    void getAllNotificationsForUserId_ReturnsList() throws Exception {
        List<NotificationResponse> responses = List.of(notificationResponse);
        when(notificationService.getAllNotificationsForUserId(userId)).thenReturn(responses);

        mockMvc.perform(get("/api/v1/notifications/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /api/v1/notifications/{notificationId}/read - Returns 200 OK")
    void markNotificationAsRead_ReturnsOk() throws Exception {
        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notificationId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully marked notification with id: " + notificationId + " as read"));

        verify(notificationService).markNotificationAsRead(notificationId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /api/v1/notifications/{userId}/read-all - Returns 200 OK")
    void markAllNotificationsAsRead_ReturnsOk() throws Exception {
        mockMvc.perform(patch("/api/v1/notifications/{userId}/read-all", userId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully marked all notifications for user id: " + userId + " as read"));

        verify(notificationService).markAllNotificationsAsRead(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/notifications/{notificationId} - Returns 200 OK")
    void deleteNotificationById_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/notifications/{notificationId}", notificationId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted notification with id: " + notificationId));

        verify(notificationService).deleteNotificationById(notificationId);
    }
}
