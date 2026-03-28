package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific compliance record cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ComplianceRecordNotFoundException extends RuntimeException{

    /**
     * Constructs a new {@code ComplianceRecordNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which compliance record was not found
     */
    public ComplianceRecordNotFoundException(String mssg) {
        super(mssg);
    }
}