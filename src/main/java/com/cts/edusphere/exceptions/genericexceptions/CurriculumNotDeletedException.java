package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a curriculum entry could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class CurriculumNotDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code CurriculumNotDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the curriculum could not be deleted
     */
    public CurriculumNotDeletedException(String mssg) {
        super(mssg);
    }
}
