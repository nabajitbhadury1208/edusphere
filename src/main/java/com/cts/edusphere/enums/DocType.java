package com.cts.edusphere.enums;

/**
 * Categorises the types of documents a student may upload to EduSphere.
 */
public enum DocType {
    /** A government-issued or institutional identity proof document. */
    IDPROOF,
    /** An official academic transcript listing course grades and credits. */
    TRANSCRIPT,
    /** A mark sheet issued after an examination. */
    MARKSSHEET,
    /** An award certificate (e.g., degree certificate, course completion). */
    CERTIFICATE,
    /** An offer letter from the institution or an employer. */
    OFFERLETTER,
    /** A bonafide certificate confirming the student's enrolment status. */
    BONAFIDE,
    /** A receipt confirming payment of fees. */
    FEE_RECEIPT,
    /** Any other document type not covered by the above categories. */
    OTHER
}
