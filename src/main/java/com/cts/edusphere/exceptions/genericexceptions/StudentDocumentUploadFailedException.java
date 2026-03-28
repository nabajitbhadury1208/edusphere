package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a student document could not be uploaded to storage due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class StudentDocumentUploadFailedException extends RuntimeException {

    /**
     * Constructs a new {@code StudentDocumentUploadFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the document upload failed
     */
    public StudentDocumentUploadFailedException(String mssg) {
        super(mssg);
    }
}
