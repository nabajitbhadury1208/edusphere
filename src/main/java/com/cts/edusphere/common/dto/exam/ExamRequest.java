package com.cts.edusphere.common.dto.exam;

import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ExamRequest(
        @NotNull(message = "CourseId is required")
        UUID courseId,

        @NotNull(message = "Type is required")
        ExamType type,

        @NotNull(message = "Date is required")
        @FutureOrPresent(message = "Exam date cannot be in the past") LocalDate date,
        @NotNull(message = "Status is required")
        Status status
) {}