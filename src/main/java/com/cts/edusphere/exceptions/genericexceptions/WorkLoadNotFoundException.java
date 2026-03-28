package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific faculty workload entry cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class WorkLoadNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code WorkLoadNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which workload entry was not found
     */
    public WorkLoadNotFoundException(String mssg) {
        super(mssg);
    }

}