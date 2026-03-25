package com.cts.edusphere.controllers.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.student.StudentService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class StudentControllerTest {

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
    private StudentService studentService;

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

    private StudentRequestDTO studentRequest;
    private StudentResponseDTO studentResponse;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        studentRequest = new StudentRequestDTO(
                "John Doe",
                "john.doe@example.com",
                "+1234567890",
                "Password123",
                LocalDate.of(2000, 5, 15),
                Gender.MALE,
                "123 Main Street"
        );

        studentResponse = new StudentResponseDTO(
                studentId,
                "John Doe",
                "john.doe@example.com",
                "+1234567890",
                Set.of(Role.STUDENT),
                Status.ACTIVE,
                LocalDate.of(2000, 5, 15),
                Gender.MALE,
                "123 Main Street",
                Instant.now(),
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/students - Returns 201 Created")
    void createStudent_ReturnsCreated() throws Exception {
        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(studentResponse);

        String requestJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phone\":\"+1234567890\"," +
                "\"password\":\"Password123\",\"dob\":\"2000-05-15\",\"gender\":\"MALE\",\"address\":\"123 Main Street\"}";

        mockMvc.perform(post("/api/v1/students").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/students - Returns 200 OK with List")
    void getAllStudents_ReturnsList() throws Exception {
        List<StudentResponseDTO> studentList = List.of(studentResponse);
        when(studentService.getAllStudents()).thenReturn(studentList);

        mockMvc.perform(get("/api/v1/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/students/{id} - Returns 200 OK")
    void getStudentById_ReturnsStudent() throws Exception {
        when(studentService.getStudentById(studentId)).thenReturn(studentResponse);

        mockMvc.perform(get("/api/v1/students/{id}", studentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/students/{id} - Returns 200 OK")
    void updateStudent_ReturnsUpdated() throws Exception {
        when(studentService.updateStudent(eq(studentId), any(StudentRequestDTO.class))).thenReturn(studentResponse);

        mockMvc.perform(put("/api/v1/students/{id}", studentId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/students/{id} - Returns 204 No Content")
    void deleteStudent_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{id}", studentId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(studentId);
    }
}
