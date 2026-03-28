package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an attempt is made to create a course that already exists in the system.
 *
 * <p>Mapped to {@code 409 Conflict} by the global exception handler.</p>
 */
public class CourseAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code CourseAlreadyExistsException} with the specified detail message.
     *
     * @param mssg the detail message identifying the duplicate course
     */
    public CourseAlreadyExistsException(String mssg) {
        super(mssg);
    }
}
