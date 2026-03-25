package com.cts.edusphere.controllers.student_documents;

import com.cts.edusphere.common.dto.student_document.StudentDocumentResponse;
import com.cts.edusphere.common.dto.student_document.VerifyDocumentRequest;
import com.cts.edusphere.config.security.JwtAuthenticationFilter;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.enums.DocType;
import com.cts.edusphere.services.student_document.StudentDocumentService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentDocumentsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.cts.edusphere.exceptions.GenericExceptionConfig.class)
public class StudentDocumentsControllerTest {

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
    private StudentDocumentService studentDocumentService;

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

    private StudentDocumentResponse documentResponse;
    private UUID documentId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        documentId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        documentResponse = new StudentDocumentResponse(
                documentId,
                studentId,
                "John Doe",
                DocType.TRANSCRIPT,
                "/api/v1/student-documents/download/" + documentId,
                false
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/student-documents/{id} - Returns 200 OK")
    void getDocument_ReturnsDocument() throws Exception {
        when(studentDocumentService.getDocumentById(documentId)).thenReturn(documentResponse);

        mockMvc.perform(get("/api/v1/student-documents/{id}", documentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(documentResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/student-documents/student/{studentId} - Returns 200 OK with List")
    void getDocumentsByStudent_ReturnsList() throws Exception {
        List<StudentDocumentResponse> documentList = List.of(documentResponse);
        when(studentDocumentService.getAllDocumentsByStudentId(studentId)).thenReturn(documentList);

        mockMvc.perform(get("/api/v1/student-documents/student/{studentId}", studentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(documentList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/student-documents/all - Returns 200 OK with List")
    void getAllDocuments_ReturnsList() throws Exception {
        List<StudentDocumentResponse> documentList = List.of(documentResponse);
        when(studentDocumentService.getAllDocuments()).thenReturn(documentList);

        mockMvc.perform(get("/api/v1/student-documents/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(documentList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /api/v1/student-documents/{id}/verify - Returns 200 OK")
    void verifyDocument_ReturnsUpdated() throws Exception {
        StudentDocumentResponse verifiedResponse = new StudentDocumentResponse(
                documentId, studentId, "John Doe", DocType.TRANSCRIPT,
                "/api/v1/student-documents/download/" + documentId, true
        );
        VerifyDocumentRequest verifyRequest = new VerifyDocumentRequest(true);
        when(studentDocumentService.verifyDocument(eq(documentId), eq(true))).thenReturn(verifiedResponse);

        mockMvc.perform(patch("/api/v1/student-documents/{id}/verify", documentId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(verifiedResponse)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/student-documents/{id} - Returns 204 No Content")
    void deleteDocument_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/student-documents/{id}", documentId).with(csrf()))
                .andExpect(status().isNoContent());

        verify(studentDocumentService).deleteDocument(documentId);
    }
}
