package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a course could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class CourseNoCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code CourseNoCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the course could not be created
     */
    public CourseNoCreatedException(String mssg) {
        super(mssg);
    }

}
