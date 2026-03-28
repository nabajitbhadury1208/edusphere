package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a file that is expected to exist in storage cannot be found.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class StorageFileNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StorageFileNotFoundException} with the specified detail message.
     *
     * @param message the detail message identifying which file was not found
     */
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code StorageFileNotFoundException} with the specified detail message and cause.
     *
     * @param message the detail message identifying which file was not found
     * @param cause   the underlying exception that led to this exception
     */
    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
