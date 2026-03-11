package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Faculty;

import java.util.UUID;

public record WorkLoadResponseDto(
        UUID facultyId,
        UUID courseId,
        Integer hours,
        String semester,
        Status status
) {
}
