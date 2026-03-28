package com.cts.edusphere.controllers.auth;

import com.cts.edusphere.common.dto.auth.*;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.config.security.TokenType;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.services.notification.NotificationService;
import com.cts.edusphere.services.user.UserServiceImpl;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class AuthControllerTest {

    @TestConfiguration
    static class TestJacksonConfig {
        @Bean
        ObjectMapper objectMapper() {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .build();
        }
    }

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private com.cts.edusphere.services.audit_log.AuditLogService auditLogService;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.ADMIN));
        user.setStatus(Status.ACTIVE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/auth/register - Returns 201 Created with tokens")
    void register_ReturnsCreatedWithTokens() throws Exception {
        String requestJson = """
                {"name":"Test User","email":"test@example.com","password":"password123","phone":"1234567890","roles":["ADMIN"]}
                """;

        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(user);
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.ACCESS)))
                .thenReturn("access-token");
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.REFRESH)))
                .thenReturn("refresh-token");

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/auth/login - Returns 200 OK with tokens")
    void login_ReturnsOkWithTokens() throws Exception {
        String requestJson = """
                {"email":"test@example.com","password":"password123"}
                """;

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com", "password123",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.ACCESS)))
                .thenReturn("access-token");
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.REFRESH)))
                .thenReturn("refresh-token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/auth/refresh - Returns 200 OK with new tokens")
    void refreshToken_ReturnsOkWithNewTokens() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");

        UserPrincipal principal = new UserPrincipal(userId, "Test User", List.of());

        when(jwtService.getUserPrincipalFromRefreshToken("valid-refresh-token")).thenReturn(principal);
        when(userService.getUserById(userId)).thenReturn(user);
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.ACCESS)))
                .thenReturn("new-access-token");
        when(jwtService.generateAccessToken(anyString(), anyString(), any(), eq(TokenType.REFRESH)))
                .thenReturn("new-refresh-token");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/auth/logout - Returns 200 OK with message")
    void logout_ReturnsOkWithMessage() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /api/v1/auth/change-password - Returns 200 OK with message")
    void changePassword_ReturnsOkWithMessage() throws Exception {
        String requestJson = """
                {"currentPassword":"oldPassword1","newPassword":"newPassword1"}
                """;

        mockMvc.perform(patch("/api/v1/auth/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }
}
