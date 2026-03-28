package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a requested resource cannot be found in the system.
 *
 * <p>This is the generic fallback for "not found" scenarios that do not have a more
 * specific exception type.  Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ResourceNotFoundException extends RuntimeException{

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message the detail message identifying which resource was not found
     */
    public ResourceNotFoundException(String message){
        super(message);
    }
}
