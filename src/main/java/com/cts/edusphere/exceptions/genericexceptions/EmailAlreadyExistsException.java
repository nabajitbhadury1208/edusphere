package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an attempt is made to register or update a user with an email
 * address that is already associated with an existing account.
 *
 * <p>Mapped to {@code 409 Conflict} by the global exception handler.</p>
 */
public class EmailAlreadyExistsException extends RuntimeException{

    /**
     * Constructs a new {@code EmailAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message identifying the duplicate email address
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
