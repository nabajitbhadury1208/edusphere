package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when the system fails to persist an audit or system log entry.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FailedToCreateLogException extends RuntimeException {

    /**
     * Constructs a new {@code FailedToCreateLogException} with the specified detail message.
     *
     * @param mssg the detail message describing why the log entry could not be created
     */
    public FailedToCreateLogException(String mssg) {
        super(mssg);
    }
}
