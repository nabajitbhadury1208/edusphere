package com.cts.edusphere.enums;

/**
 * Represents the outcome of a compliance evaluation for an entity.
 */
public enum ComplianceResult {
    /** The entity met all compliance requirements for the evaluated area. */
    PASS,
    /** The entity did not meet one or more compliance requirements. */
    FAIL,
    /** The compliance evaluation has not yet been concluded. */
    PENDING,
    /** The compliance check has been postponed to a later date. */
    DEFERRED,
    /** The compliance requirement does not apply to this entity or context. */
    NOT_APPLICABLE
}
