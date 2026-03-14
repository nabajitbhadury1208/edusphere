package com.cts.edusphere.services.report;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.report.ReportMapper;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.modules.report.Report;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.department.DepartmentRepository;
import com.cts.edusphere.repositories.report.ReportRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private UUID reportId;
    private UUID departmentId;
    private UUID generatedById;
    private Department department;
    private User generator;
    private Report report;
    private ReportRequestDto request;
    private ReportResponseDto response;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        departmentId = UUID.randomUUID();
        generatedById = UUID.randomUUID();

        department = Department.builder()
                .id(departmentId)
                .departmentName("CSE")
                .departmentCode("CSE001")
                .contactInfo("cse@edusphere.com")
                .status(Status.ACTIVE)
                .build();

        generator = User.builder()
                .id(generatedById)
                .name("Compliance Officer")
                .email("compliance@edusphere.com")
                .password("secret")
                .status(Status.ACTIVE)
                .roles(Set.of())
                .build();

        request = new ReportRequestDto(
                "{\"passPercentage\": 92}",
                Status.ACTIVE,
                ReportScope.DEPARTMENT,
                departmentId,
                generatedById
        );

        report = Report.builder()
                .id(reportId)
                .metrics("{\"passPercentage\": 92}")
                .status(Status.ACTIVE)
                .scope(ReportScope.DEPARTMENT)
                .department(department)
                .generatedBy(generator)
                .build();

        response = new ReportResponseDto(
                reportId,
                "{\"passPercentage\": 92}",
                Status.ACTIVE,
                ReportScope.DEPARTMENT,
                departmentId,
                generator
        );
    }

    @Test
    void createReport_Success() {
        when(reportMapper.toEntity(request)).thenReturn(report);
        when(reportRepository.save(any(Report.class))).thenReturn(report);
        when(reportMapper.toResponse(report)).thenReturn(response);

        ReportResponseDto result = reportService.createReport(request);

        assertNotNull(result);
        assertEquals(reportId, result.id());
        assertEquals(departmentId, result.department());
        assertEquals(ReportScope.DEPARTMENT, result.scope());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void getAllReports_ReturnsList() {
        when(reportRepository.findAll()).thenReturn(List.of(report));
        when(reportMapper.toResponse(report)).thenReturn(response);

        List<ReportResponseDto> results = reportService.getAllReports();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(reportId, results.get(0).id());
        verify(reportRepository).findAll();
    }

    @Test
    void getAllReports_ReturnsEmptyList() {
        when(reportRepository.findAll()).thenReturn(List.of());

        List<ReportResponseDto> results = reportService.getAllReports();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(reportRepository).findAll();
    }

    @Test
    void getReportById_ReturnsResponse() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportMapper.toResponse(report)).thenReturn(response);

        ReportResponseDto result = reportService.getReportById(reportId);

        assertNotNull(result);
        assertEquals(reportId, result.id());
        verify(reportRepository).findById(reportId);
    }

    @Test
    void getReportById_ThrowsException_WhenNotFound() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.getReportById(reportId));
        verify(reportRepository).findById(reportId);
    }

    @Test
    void getReportsByDepartment_ReturnsList() {
        when(reportRepository.findByDepartmentId(departmentId)).thenReturn(List.of(report));
        when(reportMapper.toResponse(report)).thenReturn(response);

        List<ReportResponseDto> results = reportService.getReportsByDepartment(departmentId);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(departmentId, results.get(0).department());
        verify(reportRepository).findByDepartmentId(departmentId);
    }

    @Test
    void getReportsByDepartment_ReturnsEmptyList() {
        when(reportRepository.findByDepartmentId(departmentId)).thenReturn(List.of());

        List<ReportResponseDto> results = reportService.getReportsByDepartment(departmentId);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(reportRepository).findByDepartmentId(departmentId);
    }

    @Test
    void updateReport_Success() {
        ReportRequestDto updatedRequest = new ReportRequestDto(
                "{\"passPercentage\": 95}",
                Status.INACTIVE,
                ReportScope.COMPLIANCE,
                departmentId,
                generatedById
        );

        Report updatedReport = Report.builder()
                .id(reportId)
                .metrics("{\"passPercentage\": 95}")
                .status(Status.INACTIVE)
                .scope(ReportScope.COMPLIANCE)
                .department(department)
                .generatedBy(generator)
                .build();

        ReportResponseDto updatedResponse = new ReportResponseDto(
                reportId,
                "{\"passPercentage\": 95}",
                Status.INACTIVE,
                ReportScope.COMPLIANCE,
                departmentId,
                generator
        );

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.save(any(Report.class))).thenReturn(updatedReport);
        when(reportMapper.toResponse(updatedReport)).thenReturn(updatedResponse);

        ReportResponseDto result = reportService.updateReport(reportId, updatedRequest);

        assertNotNull(result);
        assertEquals(Status.INACTIVE, result.status());
        assertEquals(ReportScope.COMPLIANCE, result.scope());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void updateReport_ThrowsException_WhenReportNotFound() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.updateReport(reportId, request));
        verify(reportRepository).findById(reportId);
        verify(reportRepository, never()).save(any(Report.class));
    }

    @Test
    void deleteReport_Success() {
        when(reportRepository.existsById(reportId)).thenReturn(true);

        reportService.deleteReport(reportId);

        verify(reportRepository).deleteById(reportId);
    }

    @Test
    void deleteReport_ThrowsException_WhenNotFound() {
        when(reportRepository.existsById(reportId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reportService.deleteReport(reportId));
        verify(reportRepository, never()).deleteById(reportId);
    }
}
