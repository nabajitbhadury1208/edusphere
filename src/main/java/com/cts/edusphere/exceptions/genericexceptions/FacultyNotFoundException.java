package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific faculty member cannot be located by their identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class FacultyNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code FacultyNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which faculty member was not found
     */
    public FacultyNotFoundException(String mssg) {
        super(mssg);
    }
}
