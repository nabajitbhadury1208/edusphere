package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific student account cannot be located by their identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class StudentNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StudentNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which student was not found
     */
    public StudentNotFoundException(String mssg) {
        super(mssg);
    }

}
