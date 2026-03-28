package com.cts.edusphere.services.report;


import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for report management operations within EduSphere.
 *
 * <p>Provides methods for generating, retrieving, updating, and deleting reports,
 * as well as filtering reports by their associated department. All operations use
 * Data Transfer Objects (DTOs) to decouple the service layer from the persistence model.</p>
 */
public interface ReportService {

    /**
     * Creates a new report from the provided request data.
     *
     * @param request the {@link ReportRequestDto} containing the report details; must not be {@code null}
     * @return a {@link ReportResponseDto} representing the newly created report
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if a referenced entity (e.g., department) does not exist
     */
    ReportResponseDto createReport(ReportRequestDto request);

    /**
     * Retrieves all reports in the system.
     *
     * @return a {@link List} of {@link ReportResponseDto} objects representing all reports;
     *         never {@code null}, may be empty
     */
    List<ReportResponseDto> getAllReports();

    /**
     * Retrieves a single report by its unique identifier.
     *
     * @param id the {@link UUID} of the report to retrieve
     * @return a {@link ReportResponseDto} representing the found report
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no report exists with the given ID
     */
    ReportResponseDto getReportById(UUID id);

    /**
     * Retrieves all reports associated with a specific department.
     *
     * @param departmentId the {@link UUID} of the department whose reports are to be retrieved
     * @return a {@link List} of {@link ReportResponseDto} objects for the given department;
     *         never {@code null}, may be empty if the department has no reports
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no department exists with the given ID
     */
    List<ReportResponseDto> getReportsByDepartment(UUID departmentId);

    /**
     * Updates the details of an existing report.
     *
     * @param id      the {@link UUID} of the report to update
     * @param request the {@link ReportRequestDto} containing the updated field values; must not be {@code null}
     * @return a {@link ReportResponseDto} representing the updated report
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no report exists with the given ID
     */
    ReportResponseDto updateReport(UUID id, ReportRequestDto request);

    /**
     * Deletes the report identified by the given ID.
     *
     * @param id the {@link UUID} of the report to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no report exists with the given ID
     */
    void deleteReport(UUID id);
}
