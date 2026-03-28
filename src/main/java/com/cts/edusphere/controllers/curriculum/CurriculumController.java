package com.cts.edusphere.controllers.curriculum;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.curriculum.CurriculumService;
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
@RequestMapping("/api/v1/curriculums")
@RequiredArgsConstructor
@Slf4j
/**
 * REST controller for managing academic curricula.
 * Base path: /api/v1/curriculums
 */
public class CurriculumController {

    private final CurriculumService curriculumService;

    /**
     * Creates a new curriculum record linked to a course.
     * Triggers a compliance audit for educational standards verification.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param curriculumRequest the request containing course ID, description, modules JSON, and status
     * @return HTTP 201 with the created CurriculumResponse
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<CurriculumResponse> createCurriculum(@Validated(OnCreate.class) @RequestBody CurriculumRequest curriculumRequest) {
        CurriculumResponse response = curriculumService.createCurriculum(curriculumRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all curriculum records in the system.
     * Publicly accessible (no role restriction).
     *
     * @return HTTP 200 with a list of all CurriculumResponse objects
     */
    @GetMapping
    public ResponseEntity<List<CurriculumResponse>> getAllCurriculums() {
        return ResponseEntity.ok(curriculumService.getAllCurriculums());
    }

    /**
     * Retrieves a specific curriculum record by its unique identifier.
     * Publicly accessible (no role restriction).
     *
     * @param id the UUID of the curriculum to retrieve
     * @return HTTP 200 with the matching CurriculumResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumResponse> getCurriculumById(@PathVariable UUID id) {
        return ResponseEntity.ok(curriculumService.getCurriculumById(id));
    }

    /**
     * Updates an existing curriculum record by its unique identifier.
     * Supports partial updates: only non-null fields are applied.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param id                the UUID of the curriculum to update
     * @param curriculumRequest the request body containing updated curriculum fields
     * @return HTTP 200 with a success message including the updated curriculum ID
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> updateCurriculumById(@PathVariable UUID id,
                                                       @Validated(OnUpdate.class) @RequestBody CurriculumRequest curriculumRequest) {
        curriculumService.updateCurriculumById(id, curriculumRequest);
        return ResponseEntity.ok("Successfully updated Curriculum with id: " + id);
    }

    /**
     * Permanently deletes a curriculum record by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the curriculum to delete
     * @return HTTP 200 with a success message confirming the deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteCurriculumById(@PathVariable UUID id) {
        curriculumService.deleteCurriculumById(id);
        return ResponseEntity.ok("Successfully deleted Curriculum with id: " + id);
    }
}