package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an attempt is made to create a resource that already exists in the system.
 *
 * <p>This is the generic fallback for duplicate-resource scenarios that do not have a
 * more specific exception type.  Mapped to {@code 409 Conflict} by the global exception handler.</p>
 */
public class ResourceAlreadyExistsException extends RuntimeException{

    /**
     * Constructs a new {@code ResourceAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message identifying the duplicate resource
     */
    public ResourceAlreadyExistsException(String message){
        super(message);
    }
}
