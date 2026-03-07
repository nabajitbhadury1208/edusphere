package com.cts.edusphere.controllers.researchproject;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.services.researchproject.ResearchProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research-projects")
@RequiredArgsConstructor
public class ResearchProjectController {

    private final ResearchProjectService projectService;

    // Sl No 1: Create
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ResearchProjectResponse> create(@Valid @RequestBody ResearchProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    // Sl No 2: Get All
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ResearchProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // Sl No 3: Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE') or hasRole('STUDENT')")
    public ResponseEntity<ResearchProjectResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // Sl No 6: Assign Co-investigator
    @PostMapping("/{id}/faculty")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> addFaculty(@PathVariable UUID id, @RequestParam UUID facultyId) {
        return ResponseEntity.ok(projectService.addFacultyMember(id, facultyId));
    }

    // Sl No 7: Remove Co-investigator
    @DeleteMapping("/{id}/faculty/{facultyId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> removeFaculty(@PathVariable UUID id, @PathVariable UUID facultyId) {
        return ResponseEntity.ok(projectService.removeFacultyMember(id, facultyId));
    }

    // Sl No 8: Add Student
    @PostMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> addStudent(@PathVariable UUID id, @RequestParam UUID studentId) {
        return ResponseEntity.ok(projectService.addStudent(id, studentId));
    }

    // Sl No 9: Remove Student
    @DeleteMapping("/{id}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> removeStudent(@PathVariable UUID id, @PathVariable UUID studentId) {
        return ResponseEntity.ok(projectService.removeStudent(id, studentId));
    }

    // Sl No 10: Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}