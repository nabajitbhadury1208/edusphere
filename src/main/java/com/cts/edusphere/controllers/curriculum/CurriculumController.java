package com.cts.edusphere.controllers.curriculum;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.curriculum.CurriculumService;
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
@RequestMapping("/api/v1/curriculums")
@RequiredArgsConstructor
@Slf4j
public class CurriculumController {

    private final CurriculumService curriculumService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<CurriculumResponse> createCurriculum(@Validated(OnCreate.class) @RequestBody CurriculumRequest curriculumRequest) {
        CurriculumResponse response = curriculumService.createCurriculum(curriculumRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CurriculumResponse>> getAllCurriculums() {
        return ResponseEntity.ok(curriculumService.getAllCurriculums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumResponse> getCurriculumById(@PathVariable UUID id) {
        return ResponseEntity.ok(curriculumService.getCurriculumById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> updateCurriculumById(@PathVariable UUID id,
                                                       @Validated(OnUpdate.class) @RequestBody CurriculumRequest curriculumRequest) {
        curriculumService.updateCurriculumById(id, curriculumRequest);
        return ResponseEntity.ok("Successfully updated Curriculum with id: " + id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteCurriculumById(@PathVariable UUID id) {
        curriculumService.deleteCurriculumById(id);
        return ResponseEntity.ok("Successfully deleted Curriculum with id: " + id);
    }
}