package com.cts.edusphere.services.report;


import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto request);
    List<ReportResponseDto> getAllReports();
    ReportResponseDto getReportById(UUID id);
    List<ReportResponseDto> getReportsByDepartment(UUID departmentId);
    ReportResponseDto updateReport(UUID id, ReportRequestDto request);
    void deleteReport(UUID id);
}
