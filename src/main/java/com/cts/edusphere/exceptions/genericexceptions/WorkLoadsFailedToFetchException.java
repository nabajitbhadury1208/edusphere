package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of faculty workload entries could not be retrieved due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class WorkLoadsFailedToFetchException extends RuntimeException {

    /**
     * Constructs a new {@code WorkLoadsFailedToFetchException} with the specified detail message.
     *
     * @param mssg the detail message describing why the workloads could not be fetched
     */
    public WorkLoadsFailedToFetchException(String mssg) {
        super(mssg);
    }

}