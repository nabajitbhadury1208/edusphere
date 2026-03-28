package com.cts.edusphere.enums;

/**
 * Represents the importance level of a system log or audit log entry.
 * Used to triage and filter log events during monitoring and review.
 */
public enum Severity {
    /** Informational event with no negative impact; used for routine operational messages. */
    INFO,
    /** A potentially problematic situation that warrants attention but does not yet cause failure. */
    WARN,
    /** A recoverable error that caused an operation to fail and requires investigation. */
    ERROR,
    /** A severe failure that may cause system instability or data integrity issues; requires immediate action. */
    CRITICAL
}
