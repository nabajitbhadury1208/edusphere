package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ThesisStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

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
