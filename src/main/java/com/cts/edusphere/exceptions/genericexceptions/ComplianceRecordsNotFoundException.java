package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of compliance records could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ComplianceRecordsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ComplianceRecordsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which compliance records were not found
     */
    public ComplianceRecordsNotFoundException(String mssg) {
        super(mssg);
    }
}