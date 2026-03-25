package com.cts.edusphere.controllers.curriculum;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.curriculum.CurriculumService;
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

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CurriculumController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class CurriculumControllerTest {

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
    private CurriculumService curriculumService;

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

    private CurriculumRequest curriculumRequest;
    private CurriculumResponse curriculumResponse;
    private UUID curriculumId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        curriculumId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        curriculumRequest = new CurriculumRequest(courseId, "Introduction to algorithms and data structures", "[{\"name\":\"Module 1\"}]", Status.ACTIVE);
        curriculumResponse = new CurriculumResponse(curriculumId, courseId, "Introduction to algorithms and data structures", "[{\"name\":\"Module 1\"}]", Status.ACTIVE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/curriculums - Returns 201 Created")
    void createCurriculum_ReturnsCreated() throws Exception {
        when(curriculumService.createCurriculum(any(CurriculumRequest.class))).thenReturn(curriculumResponse);

        mockMvc.perform(post("/api/v1/curriculums").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curriculumRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(curriculumResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/curriculums - Returns 200 OK with List")
    void getAllCurriculums_ReturnsList() throws Exception {
        List<CurriculumResponse> curriculumList = List.of(curriculumResponse);
        when(curriculumService.getAllCurriculums()).thenReturn(curriculumList);

        mockMvc.perform(get("/api/v1/curriculums").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(curriculumList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/curriculums/{id} - Returns 200 OK")
    void getCurriculumById_ReturnsCurriculum() throws Exception {
        when(curriculumService.getCurriculumById(curriculumId)).thenReturn(curriculumResponse);

        mockMvc.perform(get("/api/v1/curriculums/{id}", curriculumId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(curriculumResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/curriculums/{id} - Returns 200 OK")
    void updateCurriculumById_ReturnsOk() throws Exception {
        mockMvc.perform(put("/api/v1/curriculums/{id}", curriculumId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curriculumRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated Curriculum with id: " + curriculumId));

        verify(curriculumService).updateCurriculumById(eq(curriculumId), any(CurriculumRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/curriculums/{id} - Returns 200 OK")
    void deleteCurriculumById_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/curriculums/{id}", curriculumId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted Curriculum with id: " + curriculumId));

        verify(curriculumService).deleteCurriculumById(curriculumId);
    }
}
