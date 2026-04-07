package com.cts.edusphere.controllers.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.ComplianceEntityType;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.services.compliance_record.ComplianceRecordService;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = ComplianceRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class ComplianceRecordControllerTest {

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
    private ComplianceRecordService complianceRecordService;

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

    private ComplianceRecordRequest complianceRecordRequest;
    private ComplianceRecordResponse complianceRecordResponse;
    private UUID recordId;
    private UUID userId;
    private UUID entityId;

    @BeforeEach
    void setUp() {
        recordId = UUID.randomUUID();
        userId = UUID.randomUUID();
        entityId = UUID.randomUUID();

        complianceRecordRequest = new ComplianceRecordRequest(
                userId, entityId, ComplianceEntityType.COURSE,
                ComplianceResult.COMPLIANT, LocalDate.now(), "Compliance check passed");

        complianceRecordResponse = new ComplianceRecordResponse(
                recordId, userId, entityId, ComplianceEntityType.COURSE,
                ComplianceResult.COMPLIANT, LocalDate.now(), "Compliance check passed", Instant.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/compliance-records - Returns 201 Created")
    void createComplianceRecord_ReturnsCreated() throws Exception {
        when(complianceRecordService.createComplianceRecord(any(ComplianceRecordRequest.class)))
                .thenReturn(complianceRecordResponse);

        mockMvc.perform(post("/api/v1/compliance-records").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complianceRecordRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(complianceRecordResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/compliance-records - Returns 200 OK with List")
    void getAllComplianceRecords_ReturnsList() throws Exception {
        List<ComplianceRecordResponse> recordList = List.of(complianceRecordResponse);
        when(complianceRecordService.getAllComplianceRecords()).thenReturn(recordList);

        mockMvc.perform(get("/api/v1/compliance-records").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/compliance-records/{id} - Returns 200 OK")
    void getComplianceRecordById_ReturnsRecord() throws Exception {
        when(complianceRecordService.getComplianceRecordById(recordId)).thenReturn(complianceRecordResponse);

        mockMvc.perform(get("/api/v1/compliance-records/{id}", recordId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(complianceRecordResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/compliance-records/entity/{entityId} - Returns 200 OK with List")
    void getComplianceRecordsByEntityId_ReturnsList() throws Exception {
        List<ComplianceRecordResponse> recordList = List.of(complianceRecordResponse);
        when(complianceRecordService.getComplianceRecordsByEntityId(entityId)).thenReturn(recordList);

        mockMvc.perform(get("/api/v1/compliance-records/entity/{entityId}", entityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/compliance-records/user/{userId} - Returns 200 OK with List")
    void getComplianceRecordsByUserId_ReturnsList() throws Exception {
        List<ComplianceRecordResponse> recordList = List.of(complianceRecordResponse);
        when(complianceRecordService.getComplianceRecordsByUserId(userId)).thenReturn(recordList);

        mockMvc.perform(get("/api/v1/compliance-records/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/compliance-records/{id} - Returns 200 OK")
    void updateComplianceRecordById_ReturnsOk() throws Exception {
        mockMvc.perform(put("/api/v1/compliance-records/{id}", recordId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complianceRecordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated ComplianceRecord with id: " + recordId));

        verify(complianceRecordService).updateComplianceRecord(eq(recordId), any(ComplianceRecordRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/compliance-records/{id} - Returns 200 OK")
    void deleteComplianceRecordById_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/compliance-records/{id}", recordId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted ComplianceRecord with id: " + recordId));

        verify(complianceRecordService).deleteComplianceRecordById(recordId);
    }
}
