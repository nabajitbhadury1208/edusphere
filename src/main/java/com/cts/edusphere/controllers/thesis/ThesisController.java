package com.cts.edusphere.controllers.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.services.thesis.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ThesisResponseDto> create(
            @Validated(OnCreate.class) @RequestBody ThesisRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isStudent = principal.authorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        // Students can only submit a thesis for themselves — override any studentId in the body
        if (isStudent) {
            request = new ThesisRequestDto(
                    principal.userId(),
                    request.title(),
                    request.supervisorId(),
                    request.submissionDate(),
                    request.status()
            );
        }

        return ResponseEntity.ok(thesisService.createThesis(request));
    }

    /**
     * ADMIN only: see all thesis records
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ThesisResponseDto>> getAll() {
        return ResponseEntity.ok(thesisService.getAllThesis());
    }

    /**
     * STUDENT sees only their own thesis.
     * FACULTY sees only thesis where they are the supervisor.
     * ADMIN / COMPLIANCE_OFFICER / DEPARTMENT_HEAD can see any by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'DEPARTMENT_HEAD') or " +
                  "(hasRole('STUDENT') and @thesisSecurityService.isThesisStudent(#id, authentication.principal.userId)) or " +
                  "(hasRole('FACULTY') and @thesisSecurityService.isThesisSupervisor(#id, authentication.principal.userId))")
    public ResponseEntity<ThesisResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(thesisService.getThesisById(id));
    }

    /**
     * Current logged-in STUDENT → their own thesis list.
     * Current logged-in FACULTY → thesis where they are supervisor.
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY')")
    public ResponseEntity<List<ThesisResponseDto>> getMy(@AuthenticationPrincipal UserPrincipal principal) {
        boolean isFaculty = principal.authorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FACULTY"));
        if (isFaculty) {
            return ResponseEntity.ok(thesisService.getThesisBySupervisor(principal.userId()));
        }
        return ResponseEntity.ok(thesisService.getThesisByStudent(principal.userId()));
    }

    /**
     * ADMIN / FACULTY / DEPARTMENT_HEAD / COMPLIANCE_OFFICER can query any student's thesis.
     * A STUDENT can only query their own.
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER') or " +
                  "(hasRole('STUDENT') and #studentId == authentication.principal.userId)")
    public ResponseEntity<List<ThesisResponseDto>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(thesisService.getThesisByStudent(studentId));
    }

    /**
     * ADMIN / DEPARTMENT_HEAD / COMPLIANCE_OFFICER can query any supervisor's thesis.
     * A FACULTY can only query their own.
     */
    @GetMapping("/supervisor/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD', 'COMPLIANCE_OFFICER') or " +
                  "(hasRole('FACULTY') and #facultyId == authentication.principal.userId)")
    public ResponseEntity<List<ThesisResponseDto>> getBySupervisor(@PathVariable UUID facultyId) {
        return ResponseEntity.ok(thesisService.getThesisBySupervisor(facultyId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<ThesisResponseDto> update(@PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody ThesisRequestDto request) {
        return ResponseEntity.ok(thesisService.updateThesis(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        thesisService.deleteThesis(id);
        return ResponseEntity.noContent().build();
    }
}
