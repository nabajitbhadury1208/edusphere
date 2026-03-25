package com.cts.edusphere.controllers.researchproject;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.services.research_project.ResearchProjectService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ResearchProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class ResearchProjectControllerTest {

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
    private ResearchProjectService projectService;

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

    private ResearchProjectRequest projectRequest;
    private ResearchProjectResponse projectResponse;
    private UUID projectId;
    private UUID facultyId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        projectRequest = new ResearchProjectRequest(
                "AI Research Project",
                facultyId,
                List.of(),
                List.of(),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                ProjectStatus.ACTIVE
        );

        projectResponse = new ResearchProjectResponse(
                projectId,
                "AI Research Project",
                facultyId,
                ProjectStatus.ACTIVE,
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 1, 1),
                List.of(),
                List.of()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/research-projects - Returns 201 Created")
    void createProject_ReturnsCreated() throws Exception {
        when(projectService.createProject(any(ResearchProjectRequest.class))).thenReturn(projectResponse);

        mockMvc.perform(post("/api/v1/research-projects").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/research-projects - Returns 200 OK with List")
    void getAllProjects_ReturnsList() throws Exception {
        List<ResearchProjectResponse> projectList = List.of(projectResponse);
        when(projectService.getAllProjects()).thenReturn(projectList);

        mockMvc.perform(get("/api/v1/research-projects").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/research-projects/{id} - Returns 200 OK")
    void getProjectById_ReturnsProject() throws Exception {
        when(projectService.getProjectById(projectId)).thenReturn(projectResponse);

        mockMvc.perform(get("/api/v1/research-projects/{id}", projectId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/research-projects/{id}/faculty - Returns 200 OK")
    void addFaculty_ReturnsProject() throws Exception {
        when(projectService.addFacultyMember(eq(projectId), eq(facultyId))).thenReturn(projectResponse);

        mockMvc.perform(post("/api/v1/research-projects/{id}/faculty", projectId).with(csrf())
                        .param("facultyId", facultyId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/research-projects/{id}/faculty/{facultyId} - Returns 200 OK")
    void removeFaculty_ReturnsProject() throws Exception {
        when(projectService.removeFacultyMember(eq(projectId), eq(facultyId))).thenReturn(projectResponse);

        mockMvc.perform(delete("/api/v1/research-projects/{id}/faculty/{facultyId}", projectId, facultyId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/research-projects/{id}/students - Returns 200 OK")
    void addStudent_ReturnsProject() throws Exception {
        when(projectService.addStudent(eq(projectId), eq(studentId))).thenReturn(projectResponse);

        mockMvc.perform(post("/api/v1/research-projects/{id}/students", projectId).with(csrf())
                        .param("studentId", studentId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/research-projects/{id}/students/{studentId} - Returns 200 OK")
    void removeStudent_ReturnsProject() throws Exception {
        when(projectService.removeStudent(eq(projectId), eq(studentId))).thenReturn(projectResponse);

        mockMvc.perform(delete("/api/v1/research-projects/{id}/students/{studentId}", projectId, studentId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/research-projects/{id} - Returns 204 No Content")
    void deleteProject_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/research-projects/{id}", projectId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProject(projectId);
    }
}
