package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ThesisRequestDto(

        @NotNull(groups = OnCreate.class, message = "Student is required")
        UUID student,

        @NotNull(groups = OnCreate.class,message = "Title is required")
        String title,

        @NotNull(groups = OnCreate.class,message = "supervisor is required")
        UUID supervisor,

        @NotNull(groups = OnCreate.class,message = "Thesis status is required")
        ThesisStatus status
) {
}
