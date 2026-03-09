package com.cts.edusphere.controllers.department;

import com.cts.edusphere.common.dto.Department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.Department.DepartmentResponseDTO;
import com.cts.edusphere.common.dto.Faculty.FacultyResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.services.department.DepartmentService;
import com.cts.edusphere.services.faculty.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        try {
            DepartmentResponseDTO responseDTO = departmentService.createDepartment(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Failed to create department - Related resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error creating department: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        try {
            List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error fetching all departments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID id) {
        try {
            DepartmentResponseDTO responseDTO = departmentService.getDepartmentById(id);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Department not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching department {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TODO: Fix: Currently asks for deptName, contactInfo,status etc because of validation set in RequestDTO
    // Ideally shouldn't do so
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {
        try {
            DepartmentResponseDTO responseDTO = departmentService.updateDepartment(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Update failed - Department or related entity not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating department {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Todo: Fix the request param headId... maybe make it a path variable or as formdata?
    @PatchMapping("/{id}/head")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> changeDepartmentHead(
            @PathVariable UUID id,
            @RequestParam UUID headId) {
        try {
            DepartmentResponseDTO responseDTO = departmentService.changeDepartmentHead(id, headId);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Failed to change head - Department or Faculty member not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error changing department head for {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<List<FacultyResponseDTO>> getDepartmentFaculties(@PathVariable UUID id) {
        try {
            List<FacultyResponseDTO> faculties = facultyService.getFacultiesByDepartment(id);
            return ResponseEntity.ok(faculties);
        } catch (ResourceNotFoundException e) {
            log.warn("Cannot fetch faculty - Department not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching faculties for department {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Delete failed - Department not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting department {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}