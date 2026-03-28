package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of audit log entries could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class AuditLogsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code AuditLogsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which audit logs were not found
     */
    public AuditLogsNotFoundException(String mssg) {
        super(mssg);
    }
}
