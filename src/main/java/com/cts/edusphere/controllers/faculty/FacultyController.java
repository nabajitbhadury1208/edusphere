package com.cts.edusphere.controllers.faculty;

import com.cts.edusphere.common.dto.Faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.Faculty.FacultyResponseDTO;
import com.cts.edusphere.services.faculty.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyResponseDTO> createFaculty(@Valid @RequestBody FacultyRequestDTO requestDTO) {
        FacultyResponseDTO responseDTO = facultyService.createFaculty(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FacultyResponseDTO>> getAllFaculties() {
        List<FacultyResponseDTO> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDTO> getFacultyById(@PathVariable UUID id) {
        FacultyResponseDTO responseDTO = facultyService.getFacultyById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDTO> updateFaculty(
            @PathVariable UUID id,
            @Valid @RequestBody FacultyRequestDTO requestDTO) {
        FacultyResponseDTO responseDTO = facultyService.updateFaculty(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FacultyResponseDTO> partiallyUpdateFaculty(
            @PathVariable UUID id,
            @RequestBody FacultyRequestDTO requestDTO) {
        FacultyResponseDTO responseDTO = facultyService.partiallyUpdateFaculty(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable UUID id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}

