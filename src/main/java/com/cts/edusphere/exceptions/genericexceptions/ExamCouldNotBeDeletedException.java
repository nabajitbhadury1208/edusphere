package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an exam could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ExamCouldNotBeDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code ExamCouldNotBeDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the exam could not be deleted
     */
    public ExamCouldNotBeDeletedException(String mssg) {
        super(mssg);
    }

}
