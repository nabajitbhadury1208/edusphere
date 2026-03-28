package com.cts.edusphere.enums;

/**
 * Represents the grading outcome for a student's exam submission.
 */
public enum GradeStatus {
    /** The student achieved a passing score for the exam. */
    PASS,
    /** The student did not achieve a passing score for the exam. */
    FAIL,
    /** The grade has not yet been assigned or finalised by the faculty. */
    PENDING,
    /** The student did not complete the exam requirements; grade cannot be determined. */
    INCOMPLETE
}
