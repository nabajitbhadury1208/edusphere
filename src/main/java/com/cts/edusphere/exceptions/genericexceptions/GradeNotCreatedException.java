package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a grade record could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class GradeNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code GradeNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the grade could not be created
     */
    public GradeNotCreatedException(String mssg) {
        super(mssg);
    }

}
