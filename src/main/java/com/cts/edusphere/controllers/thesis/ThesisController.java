package com.cts.edusphere.controllers.thesis;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.services.thesis.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/thesis")
@RequiredArgsConstructor
public class ThesisController {
    private final ThesisService thesisService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ThesisResponseDto> create(@Validated(OnCreate.class) @RequestBody ThesisRequestDto request) {
        return ResponseEntity.ok(thesisService.createThesis(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<ThesisResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(thesisService.getThesisById(id));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'DEPT_HEAD', 'STUDENT', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponseDto>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(thesisService.getThesisByStudent(studentId));
    }

    @GetMapping("/supervisor/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE')")
    public ResponseEntity<List<ThesisResponseDto>> getBySupervisor(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(thesisService.getThesisBySupervisor(facultyId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ThesisResponseDto> update(@Validated(OnUpdate.class) @PathVariable UUID id,
            @RequestBody ThesisRequestDto request) {
        return ResponseEntity.ok(thesisService.updateThesis(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        thesisService.deleteThesis(id);
        return ResponseEntity.noContent().build();
    }
}
