package com.cts.edusphere.common.dto.thesis;

import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.modules.Student;

import java.time.LocalDate;

public record ThesisResponse(
        Student student,
        String title,
        Faculty supervisor,
        LocalDate submissionDate,
        ThesisStatus status
) {
}
