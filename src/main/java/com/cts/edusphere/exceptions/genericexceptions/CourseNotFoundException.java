package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific course cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class CourseNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code CourseNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which course was not found
     */
    public CourseNotFoundException(String mssg) {
        super(mssg);
    }
}
