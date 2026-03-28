package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an exam could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ExamNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code ExamNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the exam could not be created
     */
    public ExamNotCreatedException(String mssg) {
        super(mssg);
    }

}
