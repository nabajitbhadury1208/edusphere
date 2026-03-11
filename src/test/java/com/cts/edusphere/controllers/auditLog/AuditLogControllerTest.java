package com.cts.edusphere.controllers.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.auditLog.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
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
public class AuditLogControllerTest {

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
    private AuditLogService auditLogService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuditLogResponseDTO auditLogResponseDTO;
    private UUID userId;
    private UUID auditLogId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        auditLogId = UUID.randomUUID();

        // Matching your 6-argument Record constructor
        auditLogResponseDTO = new AuditLogResponseDTO(
                auditLogId, 
                userId, 
                "LOGIN", 
                "USER_SERVICE", 
                "2026-03-10T12:00:00Z", 
                Instant.now()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs - Returns List of Logs")
    void getAllAuditLogs_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> auditLogs = List.of(auditLogResponseDTO);
        when(auditLogService.getAllLogs()).thenReturn(auditLogs);

        mockMvc.perform(get("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditLogs)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/{id} - Returns 200 OK")
    void getAuditLogById_ReturnsLog() throws Exception {
        when(auditLogService.getLogById(auditLogId)).thenReturn(auditLogResponseDTO);

        mockMvc.perform(get("/api/v1/audit-logs/{id}", auditLogId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditLogResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/{id} - Returns 404 Not Found")
    void getAuditLogById_ReturnsNotFound() throws Exception {
        when(auditLogService.getLogById(auditLogId))
                .thenThrow(new ResourceNotFoundException("Audit log not found"));

        mockMvc.perform(get("/api/v1/audit-logs/{id}", auditLogId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audit-logs/user/{userId} - Returns User Logs")
    void getAuditLogsByUser_ReturnsList() throws Exception {
        List<AuditLogResponseDTO> logs = List.of(auditLogResponseDTO);
        when(auditLogService.getLogsByUser(userId)).thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(logs)));
    }
}