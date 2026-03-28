package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user attempts to access a resource or perform an action that
 * they are not authorised for, regardless of authentication status.
 *
 * <p>Mapped to {@code 403 Forbidden} by the global exception handler.</p>
 */
public class UnauthorizedAccessException extends RuntimeException{

    /**
     * Constructs a new {@code UnauthorizedAccessException} with the specified detail message.
     *
     * @param message the detail message describing the access violation
     */
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
