package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ThesisStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for submitting or updating a student thesis record.
 *
 * @param studentId      the UUID of the student submitting the thesis; required on create
 * @param title          the full title of the thesis; required on create
 * @param supervisorId   the UUID of the faculty member supervising the thesis; required on create
 * @param submissionDate the date on which the thesis was or will be submitted; required on create
 * @param status         the current review status of the thesis (e.g., SUBMITTED, UNDER_REVIEW, APPROVED); required on create
 */
public record ThesisRequestDto(

        @NotNull(groups = OnCreate.class, message = "Student is required")
        UUID studentId,

        @NotNull(groups = OnCreate.class,message = "Title is required")
        String title,

        @NotNull(groups = OnCreate.class,message = "supervisor is required")
        UUID supervisorId,

        @NotNull(groups = OnCreate.class,message = "submission date is required")
        LocalDate submissionDate,

        @NotNull(groups = OnCreate.class,message = "Thesis status is required")
        ThesisStatus status
) {
}
