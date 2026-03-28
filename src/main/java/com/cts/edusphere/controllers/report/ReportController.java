package com.cts.edusphere.controllers.report;


import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.report.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing academic and compliance reports.
 * Base path: /api/v1/reports
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    /**
     * Creates a new report record.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param request the request containing title, scope, department ID, generated-by user ID, and metrics
     * @return HTTP 201 with the created ReportResponseDto
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ReportResponseDto> create(@Validated(OnCreate.class) @RequestBody ReportRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    /**
     * Retrieves all report records in the system.
     * Accessible by ADMIN, COMPLIANCE, and REGULATOR roles.
     *
     * @return HTTP 200 with a list of all ReportResponseDto objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ReportResponseDto>> getAll() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    /**
     * Retrieves a specific report by its unique identifier.
     * Accessible by ADMIN, COMPLIANCE, and REGULATOR roles.
     *
     * @param id the UUID of the report to retrieve
     * @return HTTP 200 with the matching ReportResponseDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<ReportResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    /**
     * Retrieves all reports associated with a specific department.
     * Accessible by ADMIN, DEPT_HEAD, COMPLIANCE, and REGULATOR roles.
     *
     * @param departmentId the UUID of the department whose reports are to be retrieved
     * @return HTTP 200 with a list of ReportResponseDto objects for the given department
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ReportResponseDto>> getByDept(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(reportService.getReportsByDepartment(departmentId));
    }

    /**
     * Updates an existing report record by its unique identifier.
     * Supports partial updates: only non-null fields are applied.
     * Accessible by ADMIN and COMPLIANCE roles.
     *
     * @param id      the UUID of the report to update
     * @param request the request body containing updated report fields
     * @return HTTP 200 with the updated ReportResponseDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE')")
    public ResponseEntity<ReportResponseDto> update(@Validated(OnUpdate.class)@PathVariable UUID id, @Valid @RequestBody ReportRequestDto request) {
        return ResponseEntity.ok(reportService.updateReport(id, request));
    }

    /**
     * Permanently deletes a report by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the report to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
