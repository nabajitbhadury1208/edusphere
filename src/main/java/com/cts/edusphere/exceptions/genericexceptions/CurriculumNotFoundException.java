package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific curriculum entry cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class CurriculumNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code CurriculumNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which curriculum was not found
     */
    public CurriculumNotFoundException(String mssg) {
        super(mssg);
    }

}
