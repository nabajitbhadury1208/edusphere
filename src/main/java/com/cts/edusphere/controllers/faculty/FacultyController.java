package com.cts.edusphere.controllers.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.faculty.FacultyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/faculties")
@RequiredArgsConstructor
@Slf4j
/**
 * REST controller for managing faculty members.
 * Base path: /api/v1/faculties
 */
public class FacultyController {

    private final FacultyService facultyService;

    /**
     * Creates a new faculty member in the system.
     * Validates department existence, encodes password, assigns FACULTY role,
     * and triggers a compliance audit for background check.
     * Accessible by ADMIN role only.
     *
     * @param requestDTO the request containing name, email, phone, password, department ID, and position
     * @return HTTP 201 with the created FacultyResponseDTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> createFaculty(@Validated(OnCreate.class) @RequestBody FacultyRequestDTO requestDTO) {
        log.info("Initiating faculty creation for: {}", requestDTO.email());
        FacultyResponseDTO responseDTO = facultyService.createFaculty(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves all faculty members in the system.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @return HTTP 200 with a list of all FacultyResponseDTO objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<FacultyResponseDTO>> getAllFaculties() {
        log.info("Fetching all faculty members");
        List<FacultyResponseDTO> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }

    /**
     * Retrieves a specific faculty member by their unique identifier.
     * Accessible by ADMIN and DEPARTMENT_HEAD, or by the faculty member themselves
     * when the requested ID matches the authenticated user's ID.
     *
     * @param id the UUID of the faculty member to retrieve
     * @return HTTP 200 with the matching FacultyResponseDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD') or (hasRole('FACULTY') and #id == principal.userId)")
    public ResponseEntity<FacultyResponseDTO> getFacultyById(@PathVariable UUID id) {
        log.info("Fetching faculty details for ID: {}", id);
        FacultyResponseDTO responseDTO = facultyService.getFacultyById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Updates an existing faculty member's details by their unique identifier.
     * Supports partial updates: only non-null fields in the request are applied.
     * Accessible by ADMIN role only.
     *
     * @param id         the UUID of the faculty member to update
     * @param requestDTO the request body containing updated faculty fields
     * @return HTTP 200 with the updated FacultyResponseDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> updateFaculty(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody FacultyRequestDTO requestDTO) {
        log.info("Updating faculty details for ID: {}", id);
        FacultyResponseDTO responseDTO = facultyService.updateFaculty(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Permanently deletes a faculty member by their unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the faculty member to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaculty(@PathVariable UUID id) {
        log.info("Deleting faculty with ID: {}", id);
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}