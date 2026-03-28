package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user account could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class UserCreationFailedException extends RuntimeException {

    /**
     * Constructs a new {@code UserCreationFailedException} with the specified detail message.
     *
     * @param message the detail message describing why the user account could not be created
     */
    public UserCreationFailedException(String message) {
        super(message);
    }
}
