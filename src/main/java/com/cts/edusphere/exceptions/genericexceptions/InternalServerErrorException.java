package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown to represent a generic, unclassified server-side error.
 *
 * <p>Use more specific exception types where possible.  This exception acts as
 * a last-resort wrapper when no domain-specific exception is appropriate.
 * Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class InternalServerErrorException extends RuntimeException{

    /**
     * Constructs a new {@code InternalServerErrorException} with the specified detail message.
     *
     * @param message the detail message describing the internal server error
     */
    public InternalServerErrorException(String message){
        super(message);
    }
}
