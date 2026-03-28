package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when the current password supplied during a password-change request
 * does not match the stored credential, or when the new password violates
 * strength requirements.
 *
 * <p>Mapped to {@code 400 Bad Request} by the global exception handler.</p>
 */
public class InvalidPasswordException extends RuntimeException{

    /**
     * Constructs a new {@code InvalidPasswordException} with the specified detail message.
     *
     * @param message the detail message describing the password validation failure
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
