package com.cts.edusphere.common.dto.grade;


import com.cts.edusphere.enums.GradeStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object representing a grade record returned from the API.
 *
 * @param examId     the UUID of the exam to which this grade belongs
 * @param studentId  the UUID of the student who received this grade
 * @param score      the numerical score (0.0 to 100.0) achieved by the student
 * @param grade      the letter or descriptor grade assigned (e.g., "A", "B+")
 * @param status     the grading outcome status (PASS, FAIL, PENDING, or INCOMPLETE)
 * @param createdAt  the timestamp when the grade record was first created
 * @param updatedAt  the timestamp of the most recent update to the grade record
 */
@Builder
public record GradeResponse(

        UUID examId,
        UUID studentId,
        Double score,
        String grade,
        GradeStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
