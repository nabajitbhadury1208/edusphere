package com.cts.edusphere.controllers.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.audit.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuditControllerTest {
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
    private AuditService auditService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuditRequestDTO auditRequestDTO;
    private AuditResponseDTO auditResponseDTO;
    private UUID auditId;
    private UUID officerId;

    @BeforeEach
    void setUp() {
        auditId = UUID.randomUUID();
        officerId = UUID.randomUUID();

        auditRequestDTO = new AuditRequestDTO(officerId, "scope", "findings", "2022-01-22");
        auditResponseDTO = new AuditResponseDTO(auditId, officerId, "scope", "findings", LocalDate.now(),
                AuditStatus.COMPLETED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/audits - Returns 201 Created with full DTO body")
    void createAudit_ReturnsCreated() throws Exception {
        when(auditService.createAudit(any(AuditRequestDTO.class))).thenReturn(auditResponseDTO);

        mockMvc.perform(post("/api/v1/audits").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auditRequestDTO))).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(auditResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/audits - Returns Error")
    void createAudit_ReturnsBadRequest() throws Exception {
        when(auditService.createAudit(any(AuditRequestDTO.class))).thenReturn(auditResponseDTO);

        AuditRequestDTO invalidRequest = new AuditRequestDTO(null, "", "", "");

        mockMvc.perform(post("/api/v1/audits").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audits - Returns 200 OK with List")
    void getAllAudits_ReturnsList() throws Exception {
        List<AuditResponseDTO> auditList = List.of(auditResponseDTO);
        when(auditService.getAllAudits()).thenReturn(auditList);

        mockMvc.perform(get("/api/v1/audits").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audits - Returns 500 Internal Server Error")
    void getAllAudits_ReturnsInternalServerError() throws Exception {
        when(auditService.getAllAudits()).thenThrow(new InternalServerErrorException("Database connection failed"));

        mockMvc.perform(get("/api/v1/audits").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audits - Returns 200 OK with Empty List")
    void getAllAudits_ReturnsEmptyList() throws Exception {
        when(auditService.getAllAudits()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/api/v1/audits").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audits/{id} - Returns 200 OK")
    void getAuditById_ReturnsAudit() throws Exception {
        when(auditService.getAuditById(auditId)).thenReturn(auditResponseDTO);

        mockMvc.perform(get("/api/v1/audits/{id}", auditId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/audits/{id} - Returns 404 Not Found")
    void getAuditById_ReturnsNotFound() throws Exception {
        when(auditService.getAuditById(auditId)).thenThrow(new ResourceNotFoundException("Audit not found"));

        mockMvc.perform(get("/api/v1/audits/{id}", auditId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/audits/{id} - Returns 200 OK")
    void updateAudit_ReturnsUpdatedAudit() throws Exception {
        when(auditService.updateAudit(any(UUID.class), any(AuditRequestDTO.class))).thenReturn(auditResponseDTO);

        mockMvc.perform(put("/api/v1/audits/{id}", auditId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auditRequestDTO))).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/audits/{id} - Returns 204 No Content")
    void deleteAudit_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/audits/{id}", auditId).with(csrf())).andExpect(status().isNoContent());

        org.mockito.Mockito.verify(auditService).deleteAudit(auditId);
    }
}