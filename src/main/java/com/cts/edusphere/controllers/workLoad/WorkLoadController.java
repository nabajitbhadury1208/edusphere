package com.cts.edusphere.controllers.workLoad;


import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.workLoad.WorkLoadService;
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
 * REST controller for managing faculty workload records.
 * Base path: /api/v1/workload
 */
@RestController
@RequestMapping("/api/v1/workload")
@RequiredArgsConstructor
public class WorkLoadController {

    private final WorkLoadService service;

    /**
     * Creates a new workload record.
     * Accessible by ADMIN and DEPT_HEAD roles.
     *
     * @param request the workload details including faculty ID and course assignments
     * @return HTTP 201 with the created WorkLoadResponseDto
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD')")
    public ResponseEntity<WorkLoadResponseDto> create(@Validated(OnCreate.class) @RequestBody WorkLoadRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createWorkLoad(request));
    }

    /**
     * Retrieves all workload records.
     * Accessible by ADMIN, DEPT_HEAD, and COMPLIANCE roles.
     *
     * @return HTTP 200 with a list of all WorkLoadResponseDto objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'COMPLIANCE')")
    public ResponseEntity<List<WorkLoadResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAllWorkLoads());
    }

    /**
     * Retrieves a workload record by its unique identifier.
     * Accessible by ADMIN, DEPT_HEAD, and FACULTY roles.
     *
     * @param id the UUID of the workload record to retrieve
     * @return HTTP 200 with the matching WorkLoadResponseDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY')")
    public ResponseEntity<WorkLoadResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getWorkLoadById(id));
    }

    /**
     * Retrieves all workload records assigned to a specific faculty member.
     * Accessible by ADMIN, DEPT_HEAD, FACULTY, and COMPLIANCE roles.
     *
     * @param facultyId the UUID of the faculty member whose workloads to retrieve
     * @return HTTP 200 with a list of WorkLoadResponseDto objects
     */
    @GetMapping("/faculty/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE')")
    public ResponseEntity<List<WorkLoadResponseDto>> getByFaculty(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(service.getWorkLoadsByFaculty(facultyId));
    }

    /**
     * Updates a workload record (partial update).
     * Accessible by ADMIN and DEPT_HEAD roles.
     *
     * @param id the UUID of the workload record to update
     * @param request the updated workload fields
     * @return HTTP 200 with the updated WorkLoadResponseDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD')")
    public ResponseEntity<WorkLoadResponseDto> update(@Validated(OnUpdate.class)@PathVariable UUID id, @Valid @RequestBody WorkLoadRequestDto request) {
        return ResponseEntity.ok(service.updateWorkLoad(id, request));
    }

    /**
     * Permanently deletes a workload record by its unique identifier.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the workload record to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteWorkLoad(id);
        return ResponseEntity.noContent().build();
    }

}
