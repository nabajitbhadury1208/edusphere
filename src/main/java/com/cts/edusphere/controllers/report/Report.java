package com.cts.edusphere.controllers.report;


import com.cts.edusphere.common.dto.report.ReportRequest;
import com.cts.edusphere.common.dto.report.ReportResponse;
import com.cts.edusphere.services.report.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class Report {
    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ReportResponse> create(@Valid @RequestBody ReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ReportResponse>> getAll() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<ReportResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ReportResponse>> getByDept(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(reportService.getReportsByDepartment(departmentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ReportResponse> update(@PathVariable UUID id, @Valid @RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.updateReport(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
