package com.cts.edusphere.common.dto.grade;


import com.cts.edusphere.enums.GradeStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
public record GradeResponse(

        UUID examId,
        UUID studentId,
        Double score,
        String grade,
        GradeStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
