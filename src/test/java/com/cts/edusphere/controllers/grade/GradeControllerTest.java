package com.cts.edusphere.controllers.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.GradeStatus;
import com.cts.edusphere.services.grade.GradeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GradeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class GradeControllerTest {

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
    private GradeService gradeService;

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

    private GradeRequest gradeRequest;
    private GradeResponse gradeResponse;
    private UUID gradeId;
    private UUID examId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        gradeId = UUID.randomUUID();
        examId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        gradeRequest = new GradeRequest(examId, studentId, 85.0, "A", GradeStatus.PASS);
        gradeResponse = GradeResponse.builder()
                .examId(examId)
                .studentId(studentId)
                .score(85.0)
                .grade("A")
                .status(GradeStatus.PASS)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/grades - Returns 201 Created")
    void createGrade_ReturnsCreated() throws Exception {
        when(gradeService.createGrade(any(GradeRequest.class))).thenReturn(gradeResponse);

        mockMvc.perform(post("/api/v1/grades").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(gradeResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/grades - Returns 200 OK with List")
    void getAllGrades_ReturnsList() throws Exception {
        List<GradeResponse> grades = List.of(gradeResponse);
        when(gradeService.getAllGrades()).thenReturn(grades);

        mockMvc.perform(get("/api/v1/grades").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(grades)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/grades/{id} - Returns 200 OK")
    void getGradeById_ReturnsGrade() throws Exception {
        when(gradeService.getGradeById(gradeId)).thenReturn(gradeResponse);

        mockMvc.perform(get("/api/v1/grades/{id}", gradeId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(gradeResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/grades/{id} - Returns 200 OK")
    void updateGrade_ReturnsUpdated() throws Exception {
        when(gradeService.updateGrade(eq(gradeId), any(GradeRequest.class))).thenReturn(gradeResponse);

        mockMvc.perform(put("/api/v1/grades/{id}", gradeId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(gradeResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/grades/{id} - Returns 204 No Content")
    void deleteGrade_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/grades/{id}", gradeId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(gradeService).deleteGrade(gradeId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/grades/students/{studentId} - Returns 200 OK with List")
    void getGradesByStudent_ReturnsList() throws Exception {
        List<GradeResponse> grades = List.of(gradeResponse);
        when(gradeService.getGradesByStudent(studentId)).thenReturn(grades);

        mockMvc.perform(get("/api/v1/grades/students/{studentId}", studentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(grades)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/grades/exam/{examId} - Returns 200 OK with List")
    void getGradesByExam_ReturnsList() throws Exception {
        List<GradeResponse> grades = List.of(gradeResponse);
        when(gradeService.getGradesByExam(examId)).thenReturn(grades);

        mockMvc.perform(get("/api/v1/grades/exam/{examId}", examId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(grades)));
    }
}
