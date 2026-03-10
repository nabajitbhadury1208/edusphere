package com.cts.edusphere.common.dto.exam;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.UUID;


public record ExamRequest(
        @NotNull(groups = {OnCreate.class},message = "CourseId is required")
        UUID courseId,

        @NotNull(groups = {OnCreate.class},message = "Type is required")
        ExamType type,

        @NotNull(groups = {OnUpdate.class},message = "Date is required")
        @FutureOrPresent(groups = {OnUpdate.class, OnCreate.class}, message = "Exam date cannot be in the past") LocalDate date,
        @NotNull(groups = {OnCreate.class},message = "Status is required")
        Status status
) {}