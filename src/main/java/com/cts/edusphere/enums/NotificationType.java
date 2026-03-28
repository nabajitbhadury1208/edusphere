package com.cts.edusphere.enums;

/**
 * Categorises a notification by the domain event that triggered it.
 */
public enum NotificationType {
    /** Notification related to a student's enrolment or registration event. */
    ENROLLMENT,
    /** Notification related to a course update, creation, or change. */
    COURSE,
    /** Notification related to an examination schedule, result, or update. */
    EXAM,
    /** Notification related to a research project event or status change. */
    RESEARCH,
    /** Notification related to a compliance check outcome or requirement. */
    COMPLIANCE
}
