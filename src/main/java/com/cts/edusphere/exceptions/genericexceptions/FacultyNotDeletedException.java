package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a faculty member account could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FacultyNotDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code FacultyNotDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the faculty member could not be deleted
     */
    public FacultyNotDeletedException(String mssg) {
        super(mssg);
    }

}
