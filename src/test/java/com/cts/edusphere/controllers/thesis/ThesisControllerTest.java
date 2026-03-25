package com.cts.edusphere.controllers.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.services.thesis.ThesisService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ThesisController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class ThesisControllerTest {

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
    private ThesisService thesisService;

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

    private ThesisRequestDto thesisRequest;
    private ThesisResponseDto thesisResponse;
    private UUID thesisId;
    private UUID studentId;
    private UUID supervisorId;

    @BeforeEach
    void setUp() {
        thesisId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        supervisorId = UUID.randomUUID();

        thesisRequest = new ThesisRequestDto(
                studentId,
                "AI in Education",
                supervisorId,
                LocalDate.of(2026, 6, 15),
                ThesisStatus.SUBMITTED
        );

        thesisResponse = new ThesisResponseDto(
                studentId,
                "AI in Education",
                supervisorId,
                LocalDate.of(2026, 6, 15),
                ThesisStatus.SUBMITTED
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/thesis - Returns 200 OK")
    void createThesis_ReturnsOk() throws Exception {
        when(thesisService.createThesis(any(ThesisRequestDto.class))).thenReturn(thesisResponse);

        mockMvc.perform(post("/api/v1/thesis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(thesisRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(thesisResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/thesis/{id} - Returns 200 OK")
    void getThesisById_ReturnsOk() throws Exception {
        when(thesisService.getThesisById(thesisId)).thenReturn(thesisResponse);

        mockMvc.perform(get("/api/v1/thesis/{id}", thesisId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(thesisResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/thesis/student/{studentId} - Returns 200 OK with List")
    void getThesisByStudent_ReturnsList() throws Exception {
        List<ThesisResponseDto> thesisList = List.of(thesisResponse);
        when(thesisService.getThesisByStudent(studentId)).thenReturn(thesisList);

        mockMvc.perform(get("/api/v1/thesis/student/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(thesisList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/thesis/supervisor/{facultyId} - Returns 200 OK with List")
    void getThesisBySupervisor_ReturnsList() throws Exception {
        List<ThesisResponseDto> thesisList = List.of(thesisResponse);
        when(thesisService.getThesisBySupervisor(supervisorId)).thenReturn(thesisList);

        mockMvc.perform(get("/api/v1/thesis/supervisor/{facultyId}", supervisorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(thesisList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/thesis/{id} - Returns 200 OK")
    void updateThesis_ReturnsOk() throws Exception {
        when(thesisService.updateThesis(eq(thesisId), any(ThesisRequestDto.class))).thenReturn(thesisResponse);

        mockMvc.perform(put("/api/v1/thesis/{id}", thesisId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(thesisRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(thesisResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/thesis/{id} - Returns 204 No Content")
    void deleteThesis_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/thesis/{id}", thesisId))
                .andExpect(status().isNoContent());

        verify(thesisService).deleteThesis(thesisId);
    }
}
