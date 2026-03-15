package com.cts.edusphere.mappers.report;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.modules.report.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public Report toEntity(ReportRequestDto request) {
//        Report report = new Report();
//        report.setMetrics(request.metrics());
//        report.setStatus(request.status());
//        report.setScope(request.scope());
//        report.setDepartment(request.department());
//        report.setGeneratedBy(request.generatedBy());
//        return report;
        return Report.builder()
                .metrics(request.metrics())
                .status(request.status())
                .scope(request.scope())
                .department((Department.builder().id(request.departmentId()).build()))
                .build();

    }

    public ReportResponseDto toResponse(Report report) {
        return new ReportResponseDto(
                report.getId(),
                report.getMetrics(),
                report.getStatus(),
                report.getScope(),
                report.getDepartment().getId(),
                report.getGeneratedBy()
        );
    }
}
