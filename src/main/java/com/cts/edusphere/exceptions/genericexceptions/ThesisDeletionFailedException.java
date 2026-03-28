package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a thesis record could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ThesisDeletionFailedException extends RuntimeException {

    /**
     * Constructs a new {@code ThesisDeletionFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the thesis could not be deleted
     */
    public ThesisDeletionFailedException(String mssg) {
        super(mssg);
    }
}
