package com.cts.edusphere.common.dto.exam;


import com.cts.edusphere.enums.ExamType;
import com.cts.edusphere.enums.Status;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object representing an exam record returned from the API.
 *
 * @param id         the unique identifier of the exam
 * @param courseId   the UUID of the course to which this exam belongs
 * @param type       the category of the exam (MIDTERM or FINAL)
 * @param date       the scheduled date of the exam
 * @param status     the current status of the exam (ACTIVE or INACTIVE)
 * @param createdAt  the timestamp when the exam was first created in the system
 * @param updatedAt  the timestamp of the most recent modification to the exam record
 */
@Builder
public record ExamResponse(
        UUID id,
        UUID courseId,
        ExamType type,
        LocalDate date,
        Status status,
        Instant createdAt,
        Instant updatedAt
) {}

