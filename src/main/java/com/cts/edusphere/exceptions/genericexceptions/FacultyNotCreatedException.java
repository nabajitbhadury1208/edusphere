package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a faculty member account could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FacultyNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code FacultyNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the faculty member could not be created
     */
    public FacultyNotCreatedException(String mssg) {
        super(mssg);
    }

}
