package com.cts.edusphere.common.dto.exam;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for scheduling or updating an exam.
 *
 * @param courseId the UUID of the course this exam is associated with; required on create
 * @param type     the category of the exam (MIDTERM or FINAL); required on create
 * @param date     the scheduled date of the exam; must be today or a future date; required on update
 * @param status   the current status of the exam (ACTIVE or INACTIVE); required on create
 */
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