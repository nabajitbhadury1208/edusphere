package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of grade records could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class GradesNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code GradesNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which grades were not found
     */
    public GradesNotFoundException(String mssg) {
        super(mssg);
    }

}
