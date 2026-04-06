package com.cts.edusphere.controllers.researchproject;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.services.research_project.ResearchProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing research projects.
 * Base path: /api/v1/research-projects
 */
@RestController
@RequestMapping("/api/v1/research-projects")
@RequiredArgsConstructor
public class ResearchProjectController {

    private final ResearchProjectService projectService;


    /**
     * Creates a new research project.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param request the request containing title, faculty lead ID, dates, and status
     * @return HTTP 201 with the created ResearchProjectResponse
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ResearchProjectResponse> create(@Validated(OnCreate.class) @RequestBody ResearchProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }


    /**
     * Retrieves all research projects.
     * Accessible by ADMIN, DEPT_HEAD, FACULTY, COMPLIANCE, and REGULATOR roles.
     *
     * @return HTTP 200 with a list of all ResearchProjectResponse objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE', 'REGULATOR')")
    public ResponseEntity<List<ResearchProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }


    /**
     * Retrieves a specific research project by its unique identifier.
     * Accessible by ADMIN, DEPT_HEAD, FACULTY, COMPLIANCE, and STUDENT roles.
     *
     * @param id the UUID of the research project to retrieve
     * @return HTTP 200 with the matching ResearchProjectResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPT_HEAD', 'FACULTY', 'COMPLIANCE') or hasRole('STUDENT')")
    public ResponseEntity<ResearchProjectResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }


    /**
     * Adds a faculty member as a co-investigator to a research project.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id the UUID of the research project
     * @param facultyId the UUID of the faculty member to add
     * @return HTTP 200 with the updated ResearchProjectResponse
     */
    @PostMapping("/{id}/faculty")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> addFaculty(@PathVariable UUID id, @RequestParam UUID facultyId) {
        return ResponseEntity.ok(projectService.addFacultyMember(id, facultyId));
    }

    /**
     * Removes a faculty co-investigator from a research project.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id the UUID of the research project
     * @param facultyId the UUID of the faculty member to remove
     * @return HTTP 200 with the updated ResearchProjectResponse
     */
    @DeleteMapping("/{id}/faculty/{facultyId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> removeFaculty(@PathVariable UUID id, @PathVariable UUID facultyId) {
        return ResponseEntity.ok(projectService.removeFacultyMember(id, facultyId));
    }

    /**
     * Adds a student as a participant in a research project.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id the UUID of the research project
     * @param studentId the UUID of the student to add
     * @return HTTP 200 with the updated ResearchProjectResponse
     */
    @PostMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> addStudent(@PathVariable UUID id, @RequestParam UUID studentId) {
        return ResponseEntity.ok(projectService.addStudent(id, studentId));
    }


    /**
     * Removes a student participant from a research project.
     * Accessible by ADMIN and FACULTY roles.
     *
     * @param id the UUID of the research project
     * @param studentId the UUID of the student to remove
     * @return HTTP 200 with the updated ResearchProjectResponse
     */
    @DeleteMapping("/{id}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('FACULTY'))")
    public ResponseEntity<ResearchProjectResponse> removeStudent(@PathVariable UUID id, @PathVariable UUID studentId) {
        return ResponseEntity.ok(projectService.removeStudent(id, studentId));
    }


    /**
     * Permanently deletes a research project by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the research project to delete
     * @return HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }


}