package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of student accounts could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class StudentsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StudentsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which students were not found
     */
    public StudentsNotFoundException(String mssg) {
        super(mssg);
    }
}