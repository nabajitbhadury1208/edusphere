package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a single faculty workload entry could not be retrieved due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class WorkLoadFailedToFetchException extends RuntimeException {

    /**
     * Constructs a new {@code WorkLoadFailedToFetchException} with the specified detail message.
     *
     * @param mssg the detail message describing why the workload could not be fetched
     */
    public WorkLoadFailedToFetchException(String mssg) {
        super(mssg);
    }

}