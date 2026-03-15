package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.enums.ThesisStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ThesisResponseDto(
        UUID studentId,
        String title,
        UUID supervisorId,
        LocalDate submissionDate,
        ThesisStatus status
) {
}
