package com.cts.edusphere.controllers.thesis;


import com.cts.edusphere.common.dto.thesis.ThesisRequest;
import com.cts.edusphere.common.dto.thesis.ThesisResponse;
import com.cts.edusphere.services.thesis.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/thesis")
@RequiredArgsConstructor
public class Thesis {
    private final ThesisService thesisService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ThesisResponse> create(@RequestBody ThesisRequest request) {
        return ResponseEntity.ok(thesisService.createThesis(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<ThesisResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(thesisService.getThesisById(id));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'DEPT_HEAD', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponse>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(thesisService.getThesisByStudent(studentId));
    }

    @GetMapping("/supervisor/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponse>> getBySupervisor(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(thesisService.getThesisBySupervisor(facultyId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ThesisResponse> update(@PathVariable UUID id, @RequestBody ThesisRequest request) {
        return ResponseEntity.ok(thesisService.updateThesis(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        thesisService.deleteThesis(id);
        return ResponseEntity.noContent().build();
    }
}
