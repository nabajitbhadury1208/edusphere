package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a student document could not be downloaded or streamed due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class StudentDocumentDownloadFailedException extends RuntimeException {

    /**
     * Constructs a new {@code StudentDocumentDownloadFailedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the document download failed
     */
    public StudentDocumentDownloadFailedException(String mssg) {
        super(mssg);
    }

}
