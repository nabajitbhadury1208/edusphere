package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user provides an email/password combination that does not match
 * any active account in the system.
 *
 * <p>Mapped to {@code 401 Unauthorized} by the global exception handler.</p>
 */
public class InvalidCredentialsException extends RuntimeException{

    /**
     * Constructs a new {@code InvalidCredentialsException} with the specified detail message.
     *
     * @param message the detail message describing the credential failure
     */
    public InvalidCredentialsException(String message){
        super(message);
    }
}
