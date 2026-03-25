package com.cts.edusphere.controllers.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
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

@WebMvcTest(controllers = FacultyController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class FacultyControllerTest {
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

    private FacultyRequestDTO facultyRequestDTO;
    private FacultyResponseDTO facultyResponseDTO;
    private UUID facultyId;
    private UUID departmentId;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();
        departmentId = UUID.randomUUID();

        facultyRequestDTO = new FacultyRequestDTO("Dr. Smith", "smith@edu.com", "+1234567890",
                "password123", "Professor", departmentId, Status.ACTIVE);
        facultyResponseDTO = new FacultyResponseDTO(facultyId, "Dr. Smith", "smith@edu.com", "+1234567890",
                Set.of(Role.FACULTY), Status.ACTIVE, "Professor", departmentId, "Computer Science",
                Instant.now(), Instant.now(), Instant.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/faculties - Returns 201 Created")
    void createFaculty_ReturnsCreated() throws Exception {
        when(facultyService.createFaculty(any(FacultyRequestDTO.class))).thenReturn(facultyResponseDTO);

        String requestJson = String.format(
                "{\"name\":\"Dr. Smith\",\"email\":\"smith@edu.com\",\"phone\":\"+1234567890\"," +
                "\"password\":\"password123\",\"position\":\"Professor\",\"departmentId\":\"%s\",\"status\":\"ACTIVE\"}",
                departmentId);

        mockMvc.perform(post("/api/v1/faculties").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(facultyResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/faculties - Returns 200 OK with List")
    void getAllFaculties_ReturnsList() throws Exception {
        List<FacultyResponseDTO> faculties = List.of(facultyResponseDTO);
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/api/v1/faculties").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/faculties/{id} - Returns 200 OK")
    void getFacultyById_ReturnsFaculty() throws Exception {
        when(facultyService.getFacultyById(facultyId)).thenReturn(facultyResponseDTO);

        mockMvc.perform(get("/api/v1/faculties/{id}", facultyId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(facultyResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/faculties/{id} - Returns 200 OK")
    void updateFaculty_ReturnsUpdated() throws Exception {
        when(facultyService.updateFaculty(eq(facultyId), any(FacultyRequestDTO.class))).thenReturn(facultyResponseDTO);

        mockMvc.perform(put("/api/v1/faculties/{id}", facultyId).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultyRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(facultyResponseDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/faculties/{id} - Returns 204 No Content")
    void deleteFaculty_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/faculties/{id}", facultyId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(facultyService).deleteFaculty(facultyId);
    }
}
