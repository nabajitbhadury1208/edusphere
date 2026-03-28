package com.cts.edusphere.enums;

/**
 * Represents the lifecycle status of a research project.
 */
public enum ProjectStatus {
    /** The project is currently in progress with active work being performed. */
    ACTIVE,
    /** The project has been temporarily paused and is not currently progressing. */
    ON_HOLD,
    /** The project has been finished and all deliverables have been submitted. */
    COMPLETED,
    /** The project has been abandoned and will not be resumed. */
    CANCELLED
}
