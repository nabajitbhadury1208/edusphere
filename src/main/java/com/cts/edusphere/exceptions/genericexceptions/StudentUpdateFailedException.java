package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a student account could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class StudentUpdateFailedException extends RuntimeException {

    /**
     * Constructs a new {@code StudentUpdateFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the student account could not be updated
     */
    public StudentUpdateFailedException(String mssg) {
        super(mssg);
    }

}
