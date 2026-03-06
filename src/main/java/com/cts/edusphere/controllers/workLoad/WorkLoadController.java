package com.cts.edusphere.controllers.workLoad;


import com.cts.edusphere.common.dto.workload.WorkLoadRequest;
import com.cts.edusphere.common.dto.workload.WorkLoadResponse;
import com.cts.edusphere.services.workLoad.WorkLoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workload")
@RequiredArgsConstructor
public class WorkLoadController {

    private final WorkLoadService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD')")
    public ResponseEntity<WorkLoadResponse> create(@Valid @RequestBody WorkLoadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createWorkLoad(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'COMPLIANCE')")
    public ResponseEntity<List<WorkLoadResponse>> getAll() {
        return ResponseEntity.ok(service.getAllWorkLoads());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY')")
    public ResponseEntity<WorkLoadResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getWorkLoadById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE')")
    public ResponseEntity<List<WorkLoadResponse>> getByFaculty(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(service.getWorkLoadsByFaculty(facultyId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD')")
    public ResponseEntity<WorkLoadResponse> update(@PathVariable UUID id, @Valid @RequestBody WorkLoadRequest request) {
        return ResponseEntity.ok(service.updateWorkLoad(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteWorkLoad(id);
        return ResponseEntity.noContent().build();
    }

}
