package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkLoadRequestDto(

        @NotNull(groups = OnCreate.class,message = "Faculty id is required")
        UUID facultyId,

        @NotNull(groups = OnCreate.class,message = "Course id is required")
        UUID courseId,

        Integer hours,

        @NotNull(groups = OnCreate.class,message = "Semester is required")
        String semester,

        @NotNull(groups = OnCreate.class,message = "Status is required")
        Status status
) {
}
