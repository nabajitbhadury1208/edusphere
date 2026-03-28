package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a student account could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class StudentCreationFailedException extends RuntimeException {

    /**
     * Constructs a new {@code StudentCreationFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the student account could not be created
     */
    public StudentCreationFailedException(String mssg) {
        super(mssg);
    }
}
