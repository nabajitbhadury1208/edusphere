package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.report.ReportRequest;
import com.cts.edusphere.common.dto.report.ReportResponse;
import com.cts.edusphere.modules.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public Report toEntity(ReportRequest request) {
        Report report = new Report();
        report.setMetrics(request.metrics());
        report.setStatus(request.status());
        report.setScope(request.scope());
        report.setDepartment(request.department());
        report.setGeneratedBy(request.generatedBy());
        return report;
    }

    public ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getMetrics(),
                report.getStatus(),
                report.getScope(),
                report.getDepartment(),
                report.getGeneratedBy()
        );
    }
}
