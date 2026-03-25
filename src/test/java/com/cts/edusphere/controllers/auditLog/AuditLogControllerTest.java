package com.cts.edusphere.controllers.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.services.audit_log.AuditLogService;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuditLogController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class AuditLogControllerTest {

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
    private AuditLogService auditLogService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuditLogResponseDTO auditLogResponse;
    private UUID auditLogId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();
        userId = UUID.randomUUID();

        auditLogResponse = new AuditLogResponseDTO(
                auditLogId,
                userId,
                "LOGIN",
                "auth",
                Instant.parse("2026-03-25T10:00:00Z"),
                "User logged in successfully",
                SystemLogType.API_ACCESS,
                Severity.INFO
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs - Returns 200 OK with List")
    void getAllAuditLogs_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logList = List.of(auditLogResponse);
        when(auditLogService.getAllLogs()).thenReturn(logList);

        mockMvc.perform(get("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/{id} - Returns 200 OK")
    void getAuditLogById_ReturnsOk() throws Exception {
        when(auditLogService.getLogById(auditLogId)).thenReturn(auditLogResponse);

        mockMvc.perform(get("/api/v1/audit-logs/{id}", auditLogId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditLogResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/user/{userId} - Returns 200 OK with List")
    void getAuditLogsByUser_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logList = List.of(auditLogResponse);
        when(auditLogService.getLogsByUser(userId)).thenReturn(logList);

        mockMvc.perform(get("/api/v1/audit-logs/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/resource/{resource} - Returns 200 OK with List")
    void getAuditLogsByResource_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logList = List.of(auditLogResponse);
        when(auditLogService.getLogsByResource("auth")).thenReturn(logList);

        mockMvc.perform(get("/api/v1/audit-logs/resource/{resource}", "auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/severity/{severity} - Returns 200 OK with List")
    void getAuditLogsBySeverity_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logList = List.of(auditLogResponse);
        when(auditLogService.getLogsBySeverity(Severity.INFO)).thenReturn(logList);

        mockMvc.perform(get("/api/v1/audit-logs/severity/{severity}", "INFO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/type/{logType} - Returns 200 OK with List")
    void getAuditLogsByType_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logList = List.of(auditLogResponse);
        when(auditLogService.getLogsByType(SystemLogType.API_ACCESS)).thenReturn(logList);

        mockMvc.perform(get("/api/v1/audit-logs/type/{logType}", "API_ACCESS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logList)));
    }
}
