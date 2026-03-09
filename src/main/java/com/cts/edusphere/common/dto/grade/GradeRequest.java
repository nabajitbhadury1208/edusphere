package com.cts.edusphere.common.dto.grade;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.GradeStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GradeRequest(
        @NotNull(groups = {OnCreate.class},message = "ExamId is required")
        UUID examId,

        @NotNull(groups = {OnCreate.class},message = "StudentId is required")
        UUID studentId,

        @NotNull(groups = {OnCreate.class},message = "Score is required")
        @DecimalMin(groups = {OnUpdate.class,OnCreate.class},value = "0.0", message = "Score cannot be less than 0")
        @DecimalMax(groups = {OnUpdate.class,OnCreate.class},value = "100.0", message = "Score cannot be greater than 100")
        Double score,

        @NotBlank(groups = {OnUpdate.class},message = "Grade cannot be blank")
        String grade,

        @NotNull(groups = {OnCreate.class},message = "Status is required")
        GradeStatus status
) {}

