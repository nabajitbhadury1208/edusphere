package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific audit log entry cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class AuditLogNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code AuditLogNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which audit log was not found
     */
    public AuditLogNotFoundException(String mssg) {
        super(mssg);
    }
}
