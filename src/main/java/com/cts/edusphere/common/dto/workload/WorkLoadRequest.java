package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Faculty;
import jakarta.validation.constraints.NotNull;

public record WorkLoadRequest(

        @NotNull(groups = OnCreate.class,message = "Faculty is required")
        Faculty faculty,

        @NotNull(groups = OnCreate.class,message = "Course is required")
        Course course,
        Integer hours,

        @NotNull(groups = OnCreate.class,message = "Semester is required")
        String semester,

        @NotNull(groups = OnCreate.class,message = "Status is required")
        Status status
) {
}
