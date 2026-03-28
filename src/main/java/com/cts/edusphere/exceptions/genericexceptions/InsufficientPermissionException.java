package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an authenticated user attempts an action they are not authorised to perform.
 *
 * <p>Mapped to {@code 403 Forbidden} by the global exception handler.</p>
 */
public class InsufficientPermissionException extends RuntimeException{

    /**
     * Constructs a new {@code InsufficientPermissionException} with the specified detail message.
     *
     * @param message the detail message describing the missing permission or role
     */
    public InsufficientPermissionException(String message){
        super(message);
    }
}
