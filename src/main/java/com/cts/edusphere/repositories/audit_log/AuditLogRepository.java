package com.cts.edusphere.repositories.audit_log;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.modules.audit_log.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link AuditLog} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom query methods for filtering audit log entries by user, resource keyword,
 * severity, log type, combined severity and log type, and a bulk update to
 * anonymise logs when a user is deleted.</p>
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    /**
     * Retrieves all audit log entries produced by a specific user.
     *
     * <p>Uses nested property navigation on {@code user.id} to match the user association.</p>
     *
     * @param userId the {@link UUID} of the user whose audit log entries are to be fetched
     * @return a {@link List} of {@link AuditLog} instances linked to the given user;
     *         an empty list if no entries are found for that user
     */
    List<AuditLog> findByUser_Id(UUID userId);

    /**
     * Retrieves all audit log entries whose {@code resource} field contains the given keyword,
     * using a case-insensitive partial match.
     *
     * @param resource the keyword to search for within the resource field (case-insensitive)
     * @return a {@link List} of {@link AuditLog} instances whose resource contains the keyword;
     *         an empty list if no matches are found
     */
    List<AuditLog> findByResourceContainingIgnoreCase(String resource);

    /**
     * Retrieves all audit log entries with the specified severity level.
     *
     * @param severity the {@link Severity} enum value (e.g., {@code LOW}, {@code MEDIUM},
     *                 {@code HIGH}, {@code CRITICAL}) to filter by
     * @return a {@link List} of {@link AuditLog} instances matching the given severity;
     *         an empty list if no entries are found at that severity level
     */
    List<AuditLog> findBySeverity(Severity severity);

    /**
     * Retrieves all audit log entries with the specified system log type.
     *
     * @param logType the {@link SystemLogType} enum value representing the category of log
     *                (e.g., {@code AUTH}, {@code DATA_CHANGE}, {@code ACCESS}) to filter by
     * @return a {@link List} of {@link AuditLog} instances matching the given log type;
     *         an empty list if no entries are found for that log type
     */
    List<AuditLog> findByLogType(SystemLogType logType);

    /**
     * Retrieves all audit log entries that match both the specified severity level and
     * the specified system log type.
     *
     * @param severity the {@link Severity} enum value to filter by
     * @param logType  the {@link SystemLogType} enum value to filter by
     * @return a {@link List} of {@link AuditLog} instances matching both the given severity
     *         and log type; an empty list if no matches are found
     */
    List<AuditLog> findBySeverityAndLogType(Severity severity, SystemLogType logType);

    /**
     * Sets the {@code user} association to {@code null} for all audit log entries that
     * belong to the specified user, effectively anonymising those records.
     *
     * <p>Intended to be called before or during user deletion so that audit history is
     * preserved without retaining a hard reference to the deleted user.</p>
     *
     * <p>Annotated with {@link Modifying} to indicate a state-changing query, and
     * with {@link Query} to define the custom JPQL {@code UPDATE} statement.</p>
     *
     * @param userId the {@link UUID} of the user whose audit log references should be nullified
     * @Query("UPDATE AuditLog a SET a.user = null WHERE a.user.id = :userId")
     * @Modifying
     */
    @Modifying
    @Query("UPDATE AuditLog a SET a.user = null WHERE a.user.id = :userId")
    void nullifyUserOnAuditLogs(@Param("userId") UUID userId);
}
