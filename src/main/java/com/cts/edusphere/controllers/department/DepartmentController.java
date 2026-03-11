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
public class DepartmentController {

    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Validated(OnCreate.class) @RequestBody DepartmentRequestDTO requestDTO) {
        log.info("Creating new department: {}", requestDTO.departmentName());
        DepartmentResponseDTO responseDTO = departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID id) {
        log.info("Fetching department details for ID: {}", id);
        DepartmentResponseDTO responseDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody DepartmentRequestDTO requestDTO) {
        log.info("Updating department ID: {}", id);
        DepartmentResponseDTO responseDTO = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/head")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> changeDepartmentHead(
            @PathVariable UUID id,
            @RequestParam UUID headId) {
        log.info("Changing head for department ID: {} to faculty ID: {}", id, headId);
        DepartmentResponseDTO responseDTO = departmentService.changeDepartmentHead(id, headId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<List<FacultyResponseDTO>> getDepartmentFaculties(@PathVariable UUID id) {
        log.info("Fetching all faculty members for department: {}", id);
        List<FacultyResponseDTO> faculties = facultyService.getFacultiesByDepartment(id);
        return ResponseEntity.ok(faculties);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        log.info("Deleting department ID: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}