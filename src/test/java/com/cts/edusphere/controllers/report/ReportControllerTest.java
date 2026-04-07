package com.cts.edusphere.controllers.report;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.report.ReportService;
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

@WebMvcTest(controllers = ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class ReportControllerTest {

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
    private ReportService reportService;

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

    private ReportRequestDto reportRequest;
    private ReportResponseDto reportResponse;
    private UUID reportId;
    private UUID departmentId;
    private UUID generatedById;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        departmentId = UUID.randomUUID();
        generatedById = UUID.randomUUID();

        reportRequest = new ReportRequestDto("Test metrics data", Status.ACTIVE, ReportScope.DEPARTMENT_OVERVIEW, departmentId, generatedById);
        reportResponse = new ReportResponseDto(reportId, "Test metrics data", Status.ACTIVE, ReportScope.DEPARTMENT_OVERVIEW, departmentId, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/reports - Returns 201 Created")
    void createReport_ReturnsCreated() throws Exception {
        when(reportService.createReport(any(ReportRequestDto.class))).thenReturn(reportResponse);

        mockMvc.perform(post("/api/v1/reports").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(reportResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/reports - Returns 200 OK with List")
    void getAllReports_ReturnsList() throws Exception {
        List<ReportResponseDto> reports = List.of(reportResponse);
        when(reportService.getAllReports()).thenReturn(reports);

        mockMvc.perform(get("/api/v1/reports").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/reports/{id} - Returns 200 OK")
    void getReportById_ReturnsReport() throws Exception {
        when(reportService.getReportById(reportId)).thenReturn(reportResponse);

        mockMvc.perform(get("/api/v1/reports/{id}", reportId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reportResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/reports/department/{departmentId} - Returns 200 OK with List")
    void getReportsByDepartment_ReturnsList() throws Exception {
        List<ReportResponseDto> reports = List.of(reportResponse);
        when(reportService.getReportsByDepartment(departmentId)).thenReturn(reports);

        mockMvc.perform(get("/api/v1/reports/department/{departmentId}", departmentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/reports/{id} - Returns 200 OK")
    void updateReport_ReturnsUpdated() throws Exception {
        when(reportService.updateReport(eq(reportId), any(ReportRequestDto.class))).thenReturn(reportResponse);

        mockMvc.perform(put("/api/v1/reports/{id}", reportId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reportResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/reports/{id} - Returns 204 No Content")
    void deleteReport_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/reports/{id}", reportId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(reportService).deleteReport(reportId);
    }
}
