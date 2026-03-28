package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.enums.ThesisStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object representing a thesis record returned from the API.
 *
 * @param studentId      the UUID of the student who submitted the thesis
 * @param title          the full title of the thesis
 * @param supervisorId   the UUID of the supervising faculty member
 * @param submissionDate the date on which the thesis was submitted
 * @param status         the current review status of the thesis (e.g., SUBMITTED, APPROVED, REJECTED)
 */
public record ThesisResponseDto(
        UUID studentId,
        String title,
        UUID supervisorId,
        LocalDate submissionDate,
        ThesisStatus status
) {
}
