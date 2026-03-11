package com.cts.edusphere.controllers.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
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
@RequestMapping("/api/v1/faculties")
@RequiredArgsConstructor
@Slf4j
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> createFaculty(@Validated(OnCreate.class) @RequestBody FacultyRequestDTO requestDTO) {
        log.info("Initiating faculty creation for: {}", requestDTO.email());
        FacultyResponseDTO responseDTO = facultyService.createFaculty(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<List<FacultyResponseDTO>> getAllFaculties() {
        log.info("Fetching all faculty members");
        List<FacultyResponseDTO> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD') or (hasRole('FACULTY') and #id == principal.userId)")
    public ResponseEntity<FacultyResponseDTO> getFacultyById(@PathVariable UUID id) {
        log.info("Fetching faculty details for ID: {}", id);
        FacultyResponseDTO responseDTO = facultyService.getFacultyById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultyResponseDTO> updateFaculty(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody FacultyRequestDTO requestDTO) {
        log.info("Updating faculty details for ID: {}", id);
        FacultyResponseDTO responseDTO = facultyService.updateFaculty(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaculty(@PathVariable UUID id) {
        log.info("Deleting faculty with ID: {}", id);
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}