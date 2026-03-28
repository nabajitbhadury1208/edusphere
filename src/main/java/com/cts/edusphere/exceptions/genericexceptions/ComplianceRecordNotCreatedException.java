package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a compliance record could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ComplianceRecordNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code ComplianceRecordNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the compliance record could not be created
     */
    public ComplianceRecordNotCreatedException(String mssg) {
        super(mssg);
    }
}
