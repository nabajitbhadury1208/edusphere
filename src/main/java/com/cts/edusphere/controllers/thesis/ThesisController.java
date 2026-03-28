package com.cts.edusphere.controllers.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.thesis.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing student thesis records.
 * Base path: /api/v1/thesis
 */
@RestController
@RequestMapping("/api/v1/thesis")
@RequiredArgsConstructor
public class ThesisController {
    private final ThesisService thesisService;

    /**
     * Creates a new thesis record.
     * Accessible by ADMIN, FACULTY, and STUDENT roles.
     *
     * @param request the thesis details including title, student ID, and supervisor ID
     * @return HTTP 200 with the created ThesisResponseDto
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ThesisResponseDto> create(@Validated(OnCreate.class) @RequestBody ThesisRequestDto request) {
        return ResponseEntity.ok(thesisService.createThesis(request));
    }

    /**
     * Retrieves a thesis record by its unique identifier.
     * Accessible by ADMIN, FACULTY, STUDENT, and COMPLIANCE roles.
     *
     * @param id the UUID of the thesis to retrieve
     * @return HTTP 200 with the matching ThesisResponseDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<ThesisResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(thesisService.getThesisById(id));
    }

    /**
     * Retrieves all thesis records associated with a specific student.
     * Accessible by ADMIN, FACULTY, DEPT_HEAD, STUDENT, and COMPLIANCE roles.
     *
     * @param studentId the UUID of the student whose thesis records to retrieve
     * @return HTTP 200 with a list of ThesisResponseDto objects
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'DEPT_HEAD', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponseDto>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(thesisService.getThesisByStudent(studentId));
    }

    /**
     * Retrieves all thesis records supervised by a specific faculty member.
     * Accessible by ADMIN, DEPT_HEAD, FACULTY, and COMPLIANCE roles.
     *
     * @param facultyId the UUID of the supervising faculty member
     * @return HTTP 200 with a list of ThesisResponseDto objects
     */
    @GetMapping("/supervisor/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponseDto>> getBySupervisor(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(thesisService.getThesisBySupervisor(facultyId));
    }

    /**
     * Updates a thesis record (partial update).
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id the UUID of the thesis to update
     * @param request the updated thesis fields
     * @return HTTP 200 with the updated ThesisResponseDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ThesisResponseDto> update(@Validated(OnUpdate.class) @PathVariable UUID id,
            @RequestBody ThesisRequestDto request) {
        return ResponseEntity.ok(thesisService.updateThesis(id, request));
    }

    /**
     * Permanently deletes a thesis record by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the thesis to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        thesisService.deleteThesis(id);
        return ResponseEntity.noContent().build();
    }
}
