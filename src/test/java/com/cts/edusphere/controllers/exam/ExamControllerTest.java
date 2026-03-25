package com.cts.edusphere.controllers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.exam.ExamService;
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

@WebMvcTest(controllers = ExamController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class ExamControllerTest {
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
    private ExamService examService;

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

    private ExamRequest examRequest;
    private ExamResponse examResponse;
    private UUID examId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        examId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        examRequest = new ExamRequest(courseId, ExamType.MIDTERM, LocalDate.now().plusDays(30), Status.ACTIVE);
        examResponse = ExamResponse.builder()
                .id(examId)
                .courseId(courseId)
                .type(ExamType.MIDTERM)
                .date(LocalDate.now().plusDays(30))
                .status(Status.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/exams - Returns 201 Created")
    void createExam_ReturnsCreated() throws Exception {
        when(examService.createExam(any(ExamRequest.class))).thenReturn(examResponse);

        mockMvc.perform(post("/api/v1/exams").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(examResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/exams - Returns 200 OK with List")
    void getAllExams_ReturnsList() throws Exception {
        List<ExamResponse> exams = List.of(examResponse);
        when(examService.getAllExams()).thenReturn(exams);

        mockMvc.perform(get("/api/v1/exams").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(exams)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/exams/{id} - Returns 200 OK")
    void getExamById_ReturnsExam() throws Exception {
        when(examService.getExamById(examId)).thenReturn(examResponse);

        mockMvc.perform(get("/api/v1/exams/{id}", examId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(examResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/exams/{id} - Returns 200 OK")
    void updateExam_ReturnsUpdated() throws Exception {
        when(examService.updateExam(eq(examId), any(ExamRequest.class))).thenReturn(examResponse);

        mockMvc.perform(put("/api/v1/exams/{id}", examId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(examResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/exams/{id} - Returns 204 No Content")
    void deleteExam_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/exams/{id}", examId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(examService).deleteExam(examId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/exams/course/{courseId} - Returns 200 OK with List")
    void getExamsByCourse_ReturnsList() throws Exception {
        List<ExamResponse> exams = List.of(examResponse);
        when(examService.getExamsByCourse(courseId)).thenReturn(exams);

        mockMvc.perform(get("/api/v1/exams/course/{courseId}", courseId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(exams)));
    }
}
