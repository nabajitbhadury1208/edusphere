package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user's password could not be changed due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class PasswordNotChangedException extends RuntimeException{

    /**
     * Constructs a new {@code PasswordNotChangedException} with the specified detail message.
     *
     * @param message the detail message describing why the password change failed
     */
    public PasswordNotChangedException(String message) {
        super(message);
    }
}
