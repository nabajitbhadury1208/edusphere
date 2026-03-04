package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;
import jakarta.validation.constraints.NotNull;

public record ThesisRequest(

        @NotNull(message = "Student is required")
        Student student,

        @NotNull(message = "Title is required")
        String title,

        @NotNull(message = "supervisor is required")
        Faculty supervisor,

        @NotNull(message = "Thesis status is required")
        ThesisStatus status
) {
}
