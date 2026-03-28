package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an exam could not be updated due to a server-side error.
 *
 * <p>This is the checked-exception-style counterpart to {@link ExamCouldNotBeUpdated}.
 * Both are mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ExamCouldNotBeUpdatedException extends RuntimeException {

    /**
     * Constructs a new {@code ExamCouldNotBeUpdatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the exam could not be updated
     */
    public ExamCouldNotBeUpdatedException(String mssg) {
        super(mssg);
    }

}
