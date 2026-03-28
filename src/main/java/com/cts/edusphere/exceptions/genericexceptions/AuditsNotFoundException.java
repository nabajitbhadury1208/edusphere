package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of audit records could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class AuditsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code AuditsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which audits were not found
     */
    public AuditsNotFoundException(String mssg) {
        super(mssg);
    }
}
 