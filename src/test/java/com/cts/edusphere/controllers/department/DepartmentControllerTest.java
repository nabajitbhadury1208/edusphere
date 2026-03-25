package com.cts.edusphere.controllers.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.department.DepartmentService;
import com.cts.edusphere.services.faculty.FacultyService;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebMvcTest(controllers = DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class DepartmentControllerTest {
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
    private DepartmentService departmentService;

    @MockitoBean
    private FacultyService facultyService;

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

    private DepartmentRequestDTO departmentRequestDTO;
    private DepartmentResponseDTO departmentResponseDTO;
    private UUID departmentId;
    private UUID headId;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        headId = UUID.randomUUID();

        departmentRequestDTO = new DepartmentRequestDTO("Computer Science", "CS101", "cs@edu.com", Status.ACTIVE, headId);
        departmentResponseDTO = new DepartmentResponseDTO(departmentId, "Computer Science", "CS101", "cs@edu.com",
                Status.ACTIVE, headId, "Dr. Smith", Instant.now(), Instant.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/departments - Returns 201 Created")
    void createDepartment_ReturnsCreated() throws Exception {
        when(departmentService.createDepartment(any(DepartmentRequestDTO.class))).thenReturn(departmentResponseDTO);

        mockMvc.perform(post("/api/v1/departments").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(departmentResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/departments - Returns 200 OK with List")
    void getAllDepartments_ReturnsList() throws Exception {
        List<DepartmentResponseDTO> departments = List.of(departmentResponseDTO);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/v1/departments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(departments)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/departments/{id} - Returns 200 OK")
    void getDepartmentById_ReturnsDepartment() throws Exception {
        when(departmentService.getDepartmentById(departmentId)).thenReturn(departmentResponseDTO);

        mockMvc.perform(get("/api/v1/departments/{id}", departmentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(departmentResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/departments/{id} - Returns 200 OK")
    void updateDepartment_ReturnsUpdated() throws Exception {
        when(departmentService.updateDepartment(eq(departmentId), any(DepartmentRequestDTO.class))).thenReturn(departmentResponseDTO);

        mockMvc.perform(put("/api/v1/departments/{id}", departmentId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(departmentResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /api/v1/departments/{id}/head - Returns 200 OK")
    void changeDepartmentHead_ReturnsUpdated() throws Exception {
        when(departmentService.changeDepartmentHead(departmentId, headId)).thenReturn(departmentResponseDTO);

        mockMvc.perform(patch("/api/v1/departments/{id}/head", departmentId).with(csrf())
                .param("headId", headId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(departmentResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/departments/{id}/faculty - Returns 200 OK with List")
    void getDepartmentFaculties_ReturnsList() throws Exception {
        FacultyResponseDTO facultyResponseDTO = new FacultyResponseDTO(UUID.randomUUID(), "Dr. Smith", "smith@edu.com",
                "+1234567890", Set.of(Role.FACULTY), Status.ACTIVE, "Professor", departmentId, "Computer Science",
                Instant.now(), Instant.now(), Instant.now());
        List<FacultyResponseDTO> faculties = List.of(facultyResponseDTO);
        when(facultyService.getFacultiesByDepartment(departmentId)).thenReturn(faculties);

        mockMvc.perform(get("/api/v1/departments/{id}/faculty", departmentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/departments/{id} - Returns 204 No Content")
    void deleteDepartment_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/departments/{id}", departmentId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(departmentService).deleteDepartment(departmentId);
    }
}
