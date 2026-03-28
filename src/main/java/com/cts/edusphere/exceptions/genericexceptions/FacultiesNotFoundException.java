package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of faculty members could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class FacultiesNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code FacultiesNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which faculty records were not found
     */
    public FacultiesNotFoundException(String mssg) {
        super(mssg);
    }

}
