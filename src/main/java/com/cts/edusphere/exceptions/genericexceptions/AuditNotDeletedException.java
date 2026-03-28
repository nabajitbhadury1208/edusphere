package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an audit record could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class AuditNotDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code AuditNotDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the audit could not be deleted
     */
    public AuditNotDeletedException(String mssg) {
        super(mssg);
    }
}
