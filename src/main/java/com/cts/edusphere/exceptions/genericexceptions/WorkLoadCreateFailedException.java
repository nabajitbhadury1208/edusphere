package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a faculty workload assignment could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class WorkLoadCreateFailedException extends RuntimeException {

    /**
     * Constructs a new {@code WorkLoadCreateFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the workload assignment could not be created
     */
    public WorkLoadCreateFailedException(String mssg) {
        super(mssg);
    }

}