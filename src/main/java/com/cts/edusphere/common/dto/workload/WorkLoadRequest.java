package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Faculty;
import jakarta.validation.constraints.NotNull;

public record WorkLoadRequest(

        @NotNull(message = "faculty is required")
        Faculty faculty,

        @NotNull(message = "Course is required")
        Course course,
        Integer hours,

        @NotNull(message = "Semester is required")
        String semester,

        @NotNull(message = "Status is required")
        Status status
) {
}
