package com.cts.edusphere.controllers.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.workLoad.WorkLoadService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WorkLoadController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class WorkLoadControllerTest {

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
    private WorkLoadService service;

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

    private WorkLoadRequestDto workLoadRequest;
    private WorkLoadResponseDto workLoadResponse;
    private UUID workLoadId;
    private UUID facultyId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        workLoadId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        workLoadRequest = new WorkLoadRequestDto(
                facultyId,
                courseId,
                40,
                "Fall 2026",
                Status.ACTIVE
        );

        workLoadResponse = new WorkLoadResponseDto(
                facultyId,
                courseId,
                40,
                "Fall 2026",
                Status.ACTIVE
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/workload - Returns 201 Created")
    void createWorkLoad_ReturnsCreated() throws Exception {
        when(service.createWorkLoad(any(WorkLoadRequestDto.class))).thenReturn(workLoadResponse);

        mockMvc.perform(post("/api/v1/workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workLoadRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(workLoadResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/workload - Returns 200 OK with List")
    void getAllWorkLoads_ReturnsList() throws Exception {
        List<WorkLoadResponseDto> workLoadList = List.of(workLoadResponse);
        when(service.getAllWorkLoads()).thenReturn(workLoadList);

        mockMvc.perform(get("/api/v1/workload")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workLoadList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/workload/{id} - Returns 200 OK")
    void getWorkLoadById_ReturnsOk() throws Exception {
        when(service.getWorkLoadById(workLoadId)).thenReturn(workLoadResponse);

        mockMvc.perform(get("/api/v1/workload/{id}", workLoadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workLoadResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/workload/faculty/{facultyId} - Returns 200 OK with List")
    void getWorkLoadsByFaculty_ReturnsList() throws Exception {
        List<WorkLoadResponseDto> workLoadList = List.of(workLoadResponse);
        when(service.getWorkLoadsByFaculty(facultyId)).thenReturn(workLoadList);

        mockMvc.perform(get("/api/v1/workload/faculty/{facultyId}", facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workLoadList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/workload/{id} - Returns 200 OK")
    void updateWorkLoad_ReturnsOk() throws Exception {
        when(service.updateWorkLoad(eq(workLoadId), any(WorkLoadRequestDto.class))).thenReturn(workLoadResponse);

        mockMvc.perform(put("/api/v1/workload/{id}", workLoadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workLoadRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workLoadResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/workload/{id} - Returns 204 No Content")
    void deleteWorkLoad_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/workload/{id}", workLoadId))
                .andExpect(status().isNoContent());

        verify(service).deleteWorkLoad(workLoadId);
    }
}
