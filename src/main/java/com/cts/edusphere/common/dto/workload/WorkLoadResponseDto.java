package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Faculty;

public record WorkLoadResponseDto(
        Faculty faculty,
        Course course,
        Integer hours,
        String semester,
        Status status
) {
}
