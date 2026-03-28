package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific exam cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ExamNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ExamNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which exam was not found
     */
    public ExamNotFoundException(String mssg) {
        super(mssg);
    }

}
