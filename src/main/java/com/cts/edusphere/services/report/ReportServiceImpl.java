package com.cts.edusphere.services.report;

import com.cts.edusphere.common.dto.report.ReportRequestDto;
import com.cts.edusphere.common.dto.report.ReportResponseDto;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ReportCreationFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ReportDeletionFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ReportFetchingFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ReportNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ReportUpdatingFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.report.ReportMapper;
import com.cts.edusphere.modules.report.Report;
import com.cts.edusphere.repositories.department.DepartmentRepository;
import com.cts.edusphere.repositories.report.ReportRepository;
import com.cts.edusphere.repositories.user.UserRepository; // Added
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ReportService} that manages the full lifecycle of {@link Report}
 * entities within the EduSphere platform.
 *
 * <p>This service provides create, read, update, and delete (CRUD) operations for reports.
 * It coordinates between {@link ReportRepository}, {@link DepartmentRepository}, and
 * {@link UserRepository} to ensure that all relational references (department, generator)
 * are resolved to JPA-managed proxies before persistence, avoiding detached-entity errors.</p>
 *
 * <p>All public methods participate in a transaction. Read-only methods are additionally
 * annotated with {@code @Transactional(readOnly = true)} for potential performance benefits.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository; // Added

    /**
     * Creates and persists a new report from the supplied request data.
     *
     * <p>If the request contains a {@code departmentId} or {@code generatedBy} user ID,
     * JPA reference proxies are resolved for those associations to prevent detached-entity
     * exceptions and optimistic-locking issues caused by unmanaged instances.</p>
     *
     * @param request a {@link ReportRequestDto} containing all fields required to build the report
     * @return a {@link ReportResponseDto} representing the newly persisted report
     * @throws ReportCreationFailedException if a known creation error occurs
     * @throws InternalServerErrorException  if an unexpected error occurs during creation
     */
    @Override
    public ReportResponseDto createReport(ReportRequestDto request) {
        try {
            Report report = reportMapper.toEntity(request);

            // Fetch proxies to satisfy @Version and avoid Detached Entity errors
            if (request.departmentId() != null) {
                report.setDepartment(departmentRepository.getReferenceById(request.departmentId()));
            }

            // Map the generator ID to a managed Proxy
            if (request.generatedBy() != null) {
                report.setGeneratedBy(userRepository.getReferenceById(request.generatedBy()));
            }

            Report savedReport = reportRepository.save(report);
            log.info("Report created successfully with ID: {}", savedReport.getId());
            return reportMapper.toResponse(savedReport);
        } catch (ReportCreationFailedException e) {
            log.error("Error occurred while creating report: {}", e.getMessage());
            throw new ReportCreationFailedException("Failed to create report record: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating report: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while creating the report");
        }
    }

    /**
     * Retrieves all reports stored in the system.
     *
     * @return a {@link List} of {@link ReportResponseDto} objects; may be empty if no reports exist
     * @throws ReportFetchingFailedException if a known fetch error occurs
     * @throws InternalServerErrorException  if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports() {
        try {
            return reportRepository.findAll().stream()
                    .map(reportMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ReportFetchingFailedException e) {
            log.error("Error occurred while fetching all reports: {}", e.getMessage());
            throw new ReportFetchingFailedException("Failed to retrieve reports list: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all reports: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve reports list");
        }
    }

    /**
     * Retrieves a single report by its unique identifier.
     *
     * @param id the {@link UUID} of the report to retrieve
     * @return the {@link ReportResponseDto} corresponding to the found report
     * @throws ReportNotFoundException      if no report exists with the given {@code id}
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public ReportResponseDto getReportById(UUID id) {
        try {
            return reportRepository.findById(id)
                    .map(reportMapper::toResponse)
                    .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
        } catch (ReportNotFoundException e) {
            log.error("Report with id {} not found: {}", id, e.getMessage());
            throw new ReportNotFoundException("Report with id: " + id + " not found");
        } catch (Exception e) {
            log.error("Error occurred while fetching report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve report details");
        }
    }

    /**
     * Retrieves all reports associated with a specific department.
     *
     * @param departmentId the {@link UUID} of the department whose reports are to be fetched
     * @return a {@link List} of {@link ReportResponseDto} objects; may be empty if none are found
     * @throws ReportFetchingFailedException if a known fetch error occurs
     * @throws InternalServerErrorException  if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByDepartment(UUID departmentId) {
        try {
            return reportRepository.findByDepartmentId(departmentId).stream()
                    .map(reportMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ReportFetchingFailedException e) {
            log.error("Error fetching reports for department {}: {}", departmentId, e.getMessage());
            throw new ReportFetchingFailedException("Failed to retrieve department reports");
        } catch (Exception e) {
            log.error("Unexpected error fetching reports for department {}: {}", departmentId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve department reports");
        }
    }

    /**
     * Updates an existing report with the non-null fields provided in the request.
     *
     * <p>Only the {@code metrics}, {@code status}, {@code scope}, {@code departmentId}, and
     * {@code generatedBy} fields are eligible for update. Fields that are {@code null} in the
     * request are left unchanged on the existing entity. Department and user associations are
     * resolved to JPA-managed proxies before saving.</p>
     *
     * @param id      the {@link UUID} of the report to update
     * @param request a {@link ReportRequestDto} containing the fields to update; {@code null}
     *                fields are ignored
     * @return the {@link ReportResponseDto} representing the report after the update
     * @throws ResourceNotFoundException      if no report exists with the given {@code id}
     * @throws ReportUpdatingFailedException  if a known update error occurs
     * @throws InternalServerErrorException   if an unexpected error occurs during the update
     */
    @Override
    public ReportResponseDto updateReport(UUID id, ReportRequestDto request) {
        try {
            Report existing = reportRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

            if (request.metrics() != null) existing.setMetrics(request.metrics());
            if (request.status() != null) existing.setStatus(request.status());
            if (request.scope() != null) existing.setScope(request.scope());

            if (request.departmentId() != null) {
                existing.setDepartment(departmentRepository.getReferenceById(request.departmentId()));
            }

            if (request.generatedBy() != null) {
                existing.setGeneratedBy(userRepository.getReferenceById(request.generatedBy()));
            }

            Report updatedReport = reportRepository.save(existing);
            log.info("Report record updated successfully: {}", id);
            return reportMapper.toResponse(updatedReport);
        } catch (ReportUpdatingFailedException e) {
            log.error("Error occurred while updating report {}: {}", id, e.getMessage());
            throw new ReportUpdatingFailedException("Failed to update report record");
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update report record");
        }
    }

    /**
     * Permanently deletes the report identified by the given ID from the data store.
     *
     * <p>An existence check is performed before deletion; if the report does not exist, a
     * {@link ResourceNotFoundException} is thrown immediately.</p>
     *
     * @param id the {@link UUID} of the report to delete
     * @throws ResourceNotFoundException    if no report exists with the given {@code id}
     * @throws ReportDeletionFailedException if a known deletion error occurs
     * @throws InternalServerErrorException  if an unexpected error occurs during deletion
     */
    @Override
    public void deleteReport(UUID id) {
        try {
            if (!reportRepository.existsById(id)) {
                throw new ResourceNotFoundException("Report not found with id: " + id);
            }
            reportRepository.deleteById(id);
            log.info("Report record deleted successfully: {}", id);
        }  catch (ReportDeletionFailedException e) {
            log.error("Error occurred while deleting report {}: {}", id, e.getMessage());
            throw new ReportDeletionFailedException("Failed to delete report record");
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting report {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete report record");
        }
    }
}
