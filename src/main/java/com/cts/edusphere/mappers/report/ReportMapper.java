package com.cts.edusphere.mappers.report;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.modules.report.Report;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Report} entity objects
 * and their corresponding DTO representations ({@link ReportRequestDto} and
 * {@link ReportResponseDto}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever report
 * mapping is required. The department association is established using a stub entity
 * carrying only the ID from the request.</p>
 */
@Component
public class ReportMapper {

    /**
     * Converts a {@link ReportRequestDto} to a {@link Report} entity.
     *
     * <p>The department is set using a lightweight stub entity containing only the
     * department ID provided in the request. Fields such as {@code generatedBy} and
     * audit timestamps are not set here and are managed elsewhere in the application.</p>
     *
     * @param request the {@link ReportRequestDto} containing report data to map
     * @return a new {@link Report} entity built from the request data
     */
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

    /**
     * Converts a {@link Report} entity to a {@link ReportResponseDto}.
     *
     * <p>The department ID is extracted from the associated department entity.
     * All other fields are mapped directly from the report.</p>
     *
     * @param report the {@link Report} entity to convert
     * @return a {@link ReportResponseDto} populated with data from the entity
     */
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
