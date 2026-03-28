package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific audit record cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class AuditNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code AuditNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which audit was not found
     */
    public AuditNotFoundException(String mssg) {
        super(mssg);
    }
}
