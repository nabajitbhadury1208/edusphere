package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a faculty-related service or dependency cannot be found.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class FacultyServiceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code FacultyServiceNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which faculty service component was not found
     */
    public FacultyServiceNotFoundException(String mssg) {
        super(mssg);
    }

}
