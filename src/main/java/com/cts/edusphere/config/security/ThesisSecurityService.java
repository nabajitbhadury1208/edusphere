package com.cts.edusphere.config.security;

import com.cts.edusphere.repositories.thesis.ThesisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Used in @PreAuthorize SpEL expressions to check thesis ownership.
 * Referenced as @thesisSecurityService in security expressions.
 */
@Service("thesisSecurityService")
@RequiredArgsConstructor
public class ThesisSecurityService {

    private final ThesisRepository thesisRepository;

    /**
     * Returns true if the given userId is the student assigned to the thesis.
     */
    public boolean isThesisStudent(UUID thesisId, UUID userId) {
        return thesisRepository.findById(thesisId)
                .map(thesis -> thesis.getStudent().getId().equals(userId))
                .orElse(false);
    }

    /**
     * Returns true if the given userId is the supervisor (faculty) of the thesis.
     */
    public boolean isThesisSupervisor(UUID thesisId, UUID userId) {
        return thesisRepository.findById(thesisId)
                .map(thesis -> thesis.getSupervisor().getId().equals(userId))
                .orElse(false);
    }
}

