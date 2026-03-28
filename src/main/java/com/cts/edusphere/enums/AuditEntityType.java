package com.cts.edusphere.enums;

/**
 * Enumeration of entity types and operations that can be tracked in an audit record.
 * Each constant represents a discrete auditable event within the EduSphere platform.
 */
public enum AuditEntityType {
    /** A new student record was created. */
    STUDENT_CREATED,
    /** An existing student record was modified. */
    STUDENT_UPDATED,
    /** A student record was removed from the system. */
    STUDENT_DELETED,

    /** A new department was created. */
    DEPARTMENT_CREATED,
    /** An existing department record was modified. */
    DEPARTMENT_UPDATED,
    /** A department record was removed from the system. */
    DEPARTMENT_DELETED,

    /** A new faculty member record was created. */
    FACULTY_CREATED,
    /** An existing faculty member record was modified. */
    FACULTY_UPDATED,
    /** A faculty member record was removed from the system. */
    FACULTY_DELETED,

    /** A new course was created. */
    COURSE_CREATED,
    /** An existing course was modified. */
    COURSE_UPDATED,
    /** A course was removed from the system. */
    COURSE_DELETED,

    /** A new curriculum entry was created for a course. */
    CURRICULUM_CREATED,

    /** A new user account was registered in the system. */
    USER_REGISTERED,
    /** A user account was deactivated. */
    USER_DEACTIVATED,
    /** The role(s) assigned to a user account were changed. */
    USER_ROLE_CHANGED,

    /** A document was uploaded to the system. */
    DOCUMENT_UPLOADED,
    /** A previously uploaded document was verified and approved. */
    DOCUMENT_VERIFIED,
    /** A previously uploaded document was rejected. */
    DOCUMENT_REJECTED,

    /** A new exam was scheduled and created. */
    EXAM_CREATED,
    /** A grade was assigned to a student for an exam. */
    GRADE_ASSIGNED,

    /** A thesis record received an approval decision. */
    THESIS_APPROVAL,

    /** A new student document record was created. */
    STUDENT_DOCUMENT_CREATED,
    /** A student document received an approval decision. */
    STUDENT_DOCUMENT_APPROVAL,

    /** A research project received an approval decision. */
    RESEARCH_APPROVAL,

    /** A thesis record was submitted for review. */
    THESIS_REVIEW,

    /** A miscellaneous event that does not fit any other category. */
    OTHER
}
