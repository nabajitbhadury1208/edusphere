package com.cts.edusphere.services.report;


import com.cts.edusphere.common.dto.report.ReportRequest;
import com.cts.edusphere.common.dto.report.ReportResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    ReportResponse createReport(ReportRequest request);
    List<ReportResponse> getAllReports();
    ReportResponse getReportById(UUID id);
    List<ReportResponse> getReportsByDepartment(UUID departmentId);
    ReportResponse updateReport(UUID id, ReportRequest request);
    void deleteReport(UUID id);
}
