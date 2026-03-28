package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a compliance officer cannot be located by their identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class OfficerNotFoundException extends RuntimeException{

    /**
     * Constructs a new {@code OfficerNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which officer was not found
     */
    public OfficerNotFoundException(String mssg) {
        super(mssg);
    }
}
