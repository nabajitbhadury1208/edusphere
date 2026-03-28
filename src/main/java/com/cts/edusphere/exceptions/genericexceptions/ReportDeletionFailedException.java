package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an institutional report could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ReportDeletionFailedException extends RuntimeException {

    /**
     * Constructs a new {@code ReportDeletionFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the report could not be deleted
     */
    public ReportDeletionFailedException(String mssg) {
        super(mssg);
    }
}