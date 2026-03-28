package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an exam could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 *
 * @see ExamCouldNotBeUpdatedException
 */
public class ExamCouldNotBeUpdated extends RuntimeException {

    /**
     * Constructs a new {@code ExamCouldNotBeUpdated} with the specified detail message.
     *
     * @param mssg the detail message describing why the exam could not be updated
     */
    public ExamCouldNotBeUpdated(String mssg) {
        super(mssg);
    }

}
