package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a deletion operation cannot be performed, typically because the target
 * resource is referenced by other entities or is in a state that prevents deletion.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class CannotDeleteException extends RuntimeException {

    /**
     * Constructs a new {@code CannotDeleteException} with the specified detail message.
     *
     * @param message the detail message describing why the resource cannot be deleted
     */
    public CannotDeleteException(String message) {
        super(message);
    }
}
