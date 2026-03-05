package com.cts.edusphere.controllers.faculty;

import com.cts.edusphere.common.dto.Faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.Faculty.FacultyResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
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
@RequestMapping("/api/v1/faculties")
@RequiredArgsConstructor
@Slf4j
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> createFaculty(@Valid @RequestBody FacultyRequestDTO requestDTO) {
        try {
            FacultyResponseDTO responseDTO = facultyService.createFaculty(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Failed to create faculty - Related resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error creating faculty: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')")
    public ResponseEntity<List<FacultyResponseDTO>> getAllFaculties() {
        try {
            List<FacultyResponseDTO> faculties = facultyService.getAllFaculties();
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            log.error("Error fetching all faculties: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEAD') or (hasRole('FACULTY') and #id == principal.userId)")
    public ResponseEntity<FacultyResponseDTO> getFacultyById(@PathVariable UUID id) {
        try {
            FacultyResponseDTO responseDTO = facultyService.getFacultyById(id);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Faculty not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching faculty {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> updateFaculty(
            @PathVariable UUID id,
            @Valid @RequestBody FacultyRequestDTO requestDTO) {
        try {
            FacultyResponseDTO responseDTO = facultyService.updateFaculty(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.warn("Update failed - Faculty or related entity not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating faculty {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaculty(@PathVariable UUID id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Delete failed - Faculty not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting faculty {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}