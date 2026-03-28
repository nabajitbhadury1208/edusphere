package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a file-storage operation (upload, download, or deletion) fails due to an
 * I/O or configuration error.
 *
 * <p>Mapped to {@code 400 Bad Request} by the global exception handler when the failure
 * is attributable to caller-supplied input (e.g. an empty file), and to
 * {@code 500 Internal Server Error} for infrastructure-level failures.</p>
 */
public class StorageException extends RuntimeException{

    /**
     * Constructs a new {@code StorageException} with the specified detail message.
     *
     * @param message the detail message describing the storage failure
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code StorageException} with the specified detail message and cause.
     *
     * @param message the detail message describing the storage failure
     * @param cause   the underlying exception that caused this storage failure
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
