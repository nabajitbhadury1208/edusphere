package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a student document could not be deleted from storage due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class StudentDocumentDeletionFailedException extends RuntimeException {

    /**
     * Constructs a new {@code StudentDocumentDeletionFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the document could not be deleted
     */
    public StudentDocumentDeletionFailedException(String mssg) {
        super(mssg);
    }

}
