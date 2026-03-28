package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific thesis record cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ThesisNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ThesisNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which thesis was not found
     */
    public ThesisNotFoundException(String mssg) {
        super(mssg);
    }
}
