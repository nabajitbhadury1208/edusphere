package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.enums.Status;

import java.util.UUID;


//TODO NB CHECK THIS THERE IS A API FOR CHECKING THE WORKLOAD BY DATA , BUT RESPONSE DOES NOT CONTAIN UUID
public record WorkLoadResponseDto(
        UUID facultyId,
        UUID courseId,
        Integer hours,
        String semester,
        Status status
) {
}
