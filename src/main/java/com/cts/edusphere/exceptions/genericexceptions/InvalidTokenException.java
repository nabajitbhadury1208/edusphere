package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a JWT access or refresh token cannot be parsed, has an invalid
 * signature, or is otherwise malformed.
 *
 * <p>Mapped to {@code 401 Unauthorized} by the global exception handler.</p>
 */
public class InvalidTokenException extends RuntimeException{

    /**
     * Constructs a new {@code InvalidTokenException} with the specified detail message.
     *
     * @param message the detail message describing the token validation failure
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
