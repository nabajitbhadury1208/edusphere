package com.cts.edusphere.controllers.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.department.DepartmentService;
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
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
/**
 * REST controller for managing academic departments.
 * Base path: /api/v1/departments
 */
public class DepartmentController {

    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    /**
     * Creates a new department in the system.
     * Validates that the optional department head has the DEPARTMENT_HEAD or ADMIN role.
     * Accessible by ADMIN role only.
     *
     * @param requestDTO the request containing department name, code, contact info, and optional head ID
     * @return HTTP 201 with the created DepartmentResponseDTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Validated(OnCreate.class) @RequestBody DepartmentRequestDTO requestDTO) {
        log.info("Creating new department: {}", requestDTO.departmentName());
        DepartmentResponseDTO responseDTO = departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves all departments in the system.
     * Publicly accessible (no role restriction).
     *
     * @return HTTP 200 with a list of all DepartmentResponseDTO objects
     */
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * Retrieves a specific department by its unique identifier.
     * Publicly accessible (no role restriction).
     *
     * @param id the UUID of the department to retrieve
     * @return HTTP 200 with the matching DepartmentResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID id) {
        log.info("Fetching department details for ID: {}", id);
        DepartmentResponseDTO responseDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Updates an existing department's details by its unique identifier.
     * Supports partial updates: only non-null fields are applied.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param id         the UUID of the department to update
     * @param requestDTO the request body containing updated department fields
     * @return HTTP 200 with the updated DepartmentResponseDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody DepartmentRequestDTO requestDTO) {
        log.info("Updating department ID: {}", id);
        DepartmentResponseDTO responseDTO = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Changes the head of a specific department to a different user.
     * Accessible by ADMIN role only.
     *
     * @param id     the UUID of the department whose head is to be changed
     * @param headId the UUID of the new department head user
     * @return HTTP 200 with the updated DepartmentResponseDTO reflecting the new head
     */
    @PatchMapping("/{id}/head") //TODO NB CHECK THE MAPPING CONVENTIONS.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> changeDepartmentHead(
            @PathVariable UUID id,
            @RequestParam UUID headId) {
        log.info("Changing head for department ID: {} to faculty ID: {}", id, headId);
        DepartmentResponseDTO responseDTO = departmentService.changeDepartmentHead(id, headId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves all faculty members belonging to a specific department.
     * Publicly accessible (no role restriction).
     *
     * @param id the UUID of the department
     * @return HTTP 200 with a list of FacultyResponseDTO objects for the department
     */
    @GetMapping("/{id}/faculty") //TODO NB PLEASE CHECK THE MAPPING CONVENTION
    public ResponseEntity<List<FacultyResponseDTO>> getDepartmentFaculties(@PathVariable UUID id) {
        log.info("Fetching all faculty members for department: {}", id);
        List<FacultyResponseDTO> faculties = facultyService.getFacultiesByDepartment(id);
        return ResponseEntity.ok(faculties);
    }

    /**
     * Permanently deletes a department by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the department to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        log.info("Deleting department ID: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}