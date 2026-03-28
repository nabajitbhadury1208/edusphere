package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of courses could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class CoursesNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code CoursesNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which courses were not found
     */
    public CoursesNotFoundException(String mssg) {
        super(mssg);
    }
}