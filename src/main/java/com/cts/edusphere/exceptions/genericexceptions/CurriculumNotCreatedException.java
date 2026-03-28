package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a curriculum entry could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class CurriculumNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code CurriculumNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the curriculum could not be created
     */
    public CurriculumNotCreatedException(String mssg) {
        super(mssg);
    }
}
