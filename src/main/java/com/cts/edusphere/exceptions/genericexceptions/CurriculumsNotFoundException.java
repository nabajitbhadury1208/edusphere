package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of curriculum entries could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class CurriculumsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code CurriculumsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which curriculums were not found
     */
    public CurriculumsNotFoundException(String mssg) {
        super(mssg);
    }
}
