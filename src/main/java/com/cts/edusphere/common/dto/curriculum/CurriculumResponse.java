package com.cts.edusphere.common.dto.curriculum;

import com.cts.edusphere.enums.Status;

import java.util.UUID;

/**
 * Data Transfer Object representing a curriculum record returned from the API.
 *
 * @param id          the unique identifier of the curriculum entry
 * @param courseId    the UUID of the course this curriculum is associated with
 * @param description a textual overview of the curriculum content
 * @param modulesJSON a JSON-encoded string representing the structured module breakdown
 * @param status      the publish status of the curriculum (ACTIVE or INACTIVE)
 */
public record CurriculumResponse(
        UUID id,
        UUID courseId,
        String description,
        String modulesJSON,
        Status status
) {}

