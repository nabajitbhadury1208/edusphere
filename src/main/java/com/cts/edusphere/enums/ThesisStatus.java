package com.cts.edusphere.enums;

/**
 * Represents the review lifecycle status of a student thesis submission.
 */
public enum ThesisStatus {
    /** The student has submitted the thesis and it is awaiting initial review. */
    SUBMITTED,
    /** The thesis is currently being evaluated by the assigned supervisor or committee. */
    UNDER_REVIEW,
    /** The reviewer has determined that the student must make corrections before re-submission. */
    REVISION_REQUIRED,
    /** The thesis has passed all reviews and has been formally approved. */
    APPROVED,
    /** The thesis did not meet the required standards and has been rejected. */
    REJECTED
}
