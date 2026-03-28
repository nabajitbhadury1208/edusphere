package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of exams could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ExamsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ExamsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which exams were not found
     */
    public ExamsNotFoundException(String mssg) {
        super(mssg);
    }
}
