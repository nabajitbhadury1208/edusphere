package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific student document record cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class StudentDocumentNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StudentDocumentNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which student document was not found
     */
    public StudentDocumentNotFoundException(String mssg) {
        super(mssg);
    }

}
