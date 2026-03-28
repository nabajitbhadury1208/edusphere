package com.cts.edusphere.enums;

/**
 * Represents the processing status of an audit record.
 */
public enum AuditStatus {
    /** The audit has been created but not yet reviewed or concluded. */
    PENDING,
    /** The audit review process has been finished with no outstanding issues. */
    COMPLETED,
    /** The audit has been reviewed and one or more issues have been escalated for follow-up. */
    FLAGGED,
}
