package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.modules.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public Report toEntity(ReportRequestDto request) {
        Report report = new Report();
        report.setMetrics(request.metrics());
        report.setStatus(request.status());
        report.setScope(request.scope());
        report.setDepartment(request.department());
        report.setGeneratedBy(request.generatedBy());
        return report;
    }

    public ReportResponseDto toResponse(Report report) {
        return new ReportResponseDto(
                report.getId(),
                report.getMetrics(),
                report.getStatus(),
                report.getScope(),
                report.getDepartment(),
                report.getGeneratedBy()
        );
    }
}
