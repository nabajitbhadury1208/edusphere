package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user account could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class UserDeletionFailedException extends RuntimeException {

    /**
     * Constructs a new {@code UserDeletionFailedException} with the specified detail message.
     *
     * @param message the detail message describing why the user account could not be deleted
     */
    public UserDeletionFailedException(String message) {
        super(message);
    }
}
