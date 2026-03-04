package com.cts.edusphere.common.dto.grade;


import com.cts.edusphere.enums.GradeStatus;

import java.util.UUID;

public record GradeResponse(

        UUID examId,
        UUID studentId,
        Double score,
        String grade,
        GradeStatus status
) {}
