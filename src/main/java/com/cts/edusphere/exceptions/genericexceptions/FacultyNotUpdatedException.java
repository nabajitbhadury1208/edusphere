package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a faculty member account could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FacultyNotUpdatedException extends RuntimeException {

    /**
     * Constructs a new {@code FacultyNotUpdatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the faculty member could not be updated
     */
    public FacultyNotUpdatedException(String mssg) {
        super(mssg);
    }
}
