package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a file cannot be stored, retrieved, or deleted due to an I/O error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FileStorageException extends RuntimeException{

    /**
     * Constructs a new {@code FileStorageException} with the specified detail message.
     *
     * @param message the detail message describing the file storage failure
     */
    public FileStorageException(String message){
        super(message);
    }
}
