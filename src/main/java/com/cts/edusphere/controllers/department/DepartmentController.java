package com.cts.edusphere.controllers.department;

import com.cts.edusphere.common.dto.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.DepartmentResponseDTO;
import com.cts.edusphere.common.dto.FacultyResponseDTO;
import com.cts.edusphere.services.department.DepartmentService;
import com.cts.edusphere.services.faculty.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO responseDTO = departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID id) {
        DepartmentResponseDTO responseDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO responseDTO = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> partiallyUpdateDepartment(
            @PathVariable UUID id,
            @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO responseDTO = departmentService.partiallyUpdateDepartment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/head")
    public ResponseEntity<DepartmentResponseDTO> changeDepartmentHead(
            @PathVariable UUID id,
            @RequestParam UUID headId) {
        DepartmentResponseDTO responseDTO = departmentService.changeDepartmentHead(id, headId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<List<FacultyResponseDTO>> getDepartmentFaculties(@PathVariable UUID id) {
        List<FacultyResponseDTO> faculties = facultyService.getFacultiesByDepartment(id);
        return ResponseEntity.ok(faculties);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}

