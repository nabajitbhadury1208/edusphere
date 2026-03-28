package com.cts.edusphere.common.dto.workload;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

/**
 * Data Transfer Object representing a faculty workload record returned from the API.
 *
 * <p><strong>Note:</strong> There is a pending check (TODO) regarding a workload-by-date API
 * whose response does not include a UUID field.</p>
 *
 * @param facultyId the UUID of the faculty member this workload belongs to
 * @param courseId  the UUID of the course associated with this workload entry
 * @param hours     the number of teaching hours assigned for this workload
 * @param semester  the academic semester to which this workload applies
 * @param status    the current status of this workload assignment (ACTIVE or INACTIVE)
 */
//TODO NB CHECK THIS THERE IS A API FOR CHECKING THE WORKLOAD BY DATA , BUT RESPONSE DOES NOT CONTAIN UUID
public record WorkLoadResponseDto(
        UUID facultyId,
        UUID courseId,
        Integer hours,
        String semester,
        Status status
) {
}
