package com.cts.edusphere.services.report;

import com.cts.edusphere.common.dto.report.ReportRequest;
import com.cts.edusphere.common.dto.report.ReportResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ReportMapper;
import com.cts.edusphere.modules.Report;
import com.cts.edusphere.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        return reportMapper.toResponse(reportRepository.save(reportMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ReportResponse getReportById(UUID id) {
        return reportRepository.findById(id).map(reportMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
    }

    @Override
    @Transactional
    public List<ReportResponse> getReportsByDepartment(UUID departmentId) {
        return reportRepository.findByDepartmentId(departmentId).stream()
                .map(reportMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ReportResponse updateReport(UUID id, ReportRequest request) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));

        // Update fields matching your record
        existing.setMetrics(request.metrics());
        existing.setStatus(request.status());
        existing.setScope(request.scope());
        existing.setDepartment(request.department());

        return reportMapper.toResponse(reportRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteReport(UUID id) {
        if (!reportRepository.existsById(id)) throw new ResourceNotFoundException("Report not found");
        reportRepository.deleteById(id);
    }
}
