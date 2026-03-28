package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a grade record could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class GradeCouldNotBeDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code GradeCouldNotBeDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the grade could not be deleted
     */
    public GradeCouldNotBeDeletedException(String mssg) {
        super(mssg);
    }

}
