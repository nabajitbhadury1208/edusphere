package com.cts.edusphere.enums;

/**
 * Defines the access roles available within the EduSphere platform.
 * Roles determine what actions a user is permitted to perform.
 */
public enum Role {
    /** A learner enrolled in courses; can view grades, submit documents, and access curricula. */
    STUDENT,
    /** An academic staff member; can manage courses, assign grades, and supervise theses. */
    FACULTY,
    /** Head of an academic department; can manage department resources and faculty workloads. */
    DEPARTMENT_HEAD,
    /** A system administrator; has full access to all platform management functions. */
    ADMIN,
    /** An officer responsible for monitoring and recording compliance evaluations. */
    COMPLIANCE_OFFICER,
    /** An external oversight user who reviews institutional compliance and audit reports. */
    REGULATOR
}

