package com.cts.edusphere.enums;

/**
 * General-purpose activation status used across multiple entities in EduSphere
 * (e.g., users, courses, departments, workloads).
 */
public enum Status {
    /** The entity is enabled and operational within the system. */
    ACTIVE,
    /** The entity has been disabled and is no longer accessible or in use. */
    INACTIVE
}
