package com.cts.edusphere.common.dto.grade;

import com.cts.edusphere.enums.GradeStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GradeRequest(
        @NotNull(message = "ExamId is required")
        UUID examId,

        @NotNull(message = "StudentId is required")
        UUID studentId,

        @NotNull(message = "Score is required")
        @DecimalMin(value = "0.0", message = "Score cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Score cannot be greater than 100")
        Double score,

        @NotBlank(message = "Grade cannot be blank")
        String grade,

        @NotNull(message = "Status is required")
        GradeStatus status
) {}