package com.cts.edusphere.controllers.course;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.course.CourseService;
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

@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.context.annotation.Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class CourseControllerTest {

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
    private CourseService courseService;

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

    private CourseRequest courseRequest;
    private CourseResponse courseResponse;
    private UUID courseId;
    private UUID departmentId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        departmentId = UUID.randomUUID();

        courseRequest = new CourseRequest("Introduction to CS", departmentId, 3, 60, Status.ACTIVE);
        courseResponse = new CourseResponse(courseId, "Introduction to CS", departmentId, "Computer Science", 3, 60, Status.ACTIVE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/courses - Returns 201 Created")
    void createCourse_ReturnsCreated() throws Exception {
        when(courseService.createCourse(any(CourseRequest.class))).thenReturn(courseResponse);

        mockMvc.perform(post("/api/v1/courses").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successfully created course"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/courses - Returns 200 OK with List")
    void getAllCourses_ReturnsList() throws Exception {
        List<CourseResponse> courseList = List.of(courseResponse);
        when(courseService.getAllCourses()).thenReturn(courseList);

        mockMvc.perform(get("/api/v1/courses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/courses/{id} - Returns 200 OK")
    void getCourseById_ReturnsCourse() throws Exception {
        when(courseService.getCourseById(courseId)).thenReturn(courseResponse);

        mockMvc.perform(get("/api/v1/courses/{id}", courseId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/courses/{id} - Returns 200 OK")
    void updateCourseById_ReturnsOk() throws Exception {
        when(courseService.updateCourse(eq(courseId), any(CourseRequest.class))).thenReturn(courseResponse);

        mockMvc.perform(put("/api/v1/courses/{id}", courseId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated user with id: " + courseId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/courses/{id}/status - Returns 200 OK")
    void setActivate_ReturnsOk() throws Exception {
        mockMvc.perform(put("/api/v1/courses/{id}/status", courseId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Status.ACTIVE)))
                .andExpect(status().isOk());

        verify(courseService).setActivateDeactivate(eq(courseId), any(Status.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/courses/{id} - Returns 200 OK")
    void deleteCourseById_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/courses/{id}", courseId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Deleted User with Id:" + courseId));

        verify(courseService).deleteCourseById(courseId);
    }
}
