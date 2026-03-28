package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a JWT access or refresh token has passed its expiry time and
 * can no longer be accepted as valid.
 *
 * <p>Mapped to {@code 401 Unauthorized} by the global exception handler.</p>
 */
public class TokenExpiredException extends RuntimeException{

    /**
     * Constructs a new {@code TokenExpiredException} with the specified detail message.
     *
     * @param message the detail message indicating which token has expired
     */
    public TokenExpiredException(String message) {
        super(message);
    }
}
