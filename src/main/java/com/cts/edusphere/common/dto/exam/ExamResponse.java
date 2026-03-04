package com.cts.edusphere.common.dto.exam;


import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record ExamResponse(
        UUID id,
        UUID courseId,
        ExamType type,
        LocalDate date,
        Status status
) {}

