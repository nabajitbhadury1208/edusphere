package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of student document records could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class StudentDocumentsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StudentDocumentsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which student documents were not found
     */
    public StudentDocumentsNotFoundException(String mssg) {
        super(mssg);
    }

}
