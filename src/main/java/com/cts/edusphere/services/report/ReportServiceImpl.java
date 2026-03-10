package com.cts.edusphere.services.report;

import com.cts.edusphere.common.dto.report.ReportRequest;
import com.cts.edusphere.common.dto.report.ReportResponse;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ReportMapper;
import com.cts.edusphere.modules.Report;
import com.cts.edusphere.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public ReportResponse createReport(ReportRequest request) {
        try {
            Report report = reportMapper.toEntity(request);
            Report savedReport = reportRepository.save(report);
            log.info("Report created successfully with ID: {}", savedReport.getId());
            return reportMapper.toResponse(savedReport);
        } catch (Exception e) {
            log.error("Error occurred while creating report: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create report record");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        try {
            return reportRepository.findAll().stream()
                    .map(reportMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching all reports: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve reports list");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getReportById(UUID id) {
        try {
            return reportRepository.findById(id)
                    .map(reportMapper::toResponse)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while fetching report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve report details");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getReportsByDepartment(UUID departmentId) {
        try {
            return reportRepository.findByDepartmentId(departmentId).stream()
                    .map(reportMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching reports for department {}: {}", departmentId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve department reports");
        }
    }

    @Override
    public ReportResponse updateReport(UUID id, ReportRequest request) {
        try {
            Report existing = reportRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

            if (request.metrics() != null) existing.setMetrics(request.metrics());
            if (request.status() != null) existing.setStatus(request.status());
            if (request.scope() != null) existing.setScope(request.scope());
            if (request.department() != null) existing.setDepartment(request.department());

            Report updatedReport = reportRepository.save(existing);
            log.info("Report record updated successfully: {}", id);
            return reportMapper.toResponse(updatedReport);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update report record");
        }
    }

    @Override
    public void deleteReport(UUID id) {
        try {
            if (!reportRepository.existsById(id)) {
                throw new ResourceNotFoundException("Report not found with id: " + id);
            }
            reportRepository.deleteById(id);
            log.info("Report record deleted successfully: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while deleting report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete report record");
        }
    }
}