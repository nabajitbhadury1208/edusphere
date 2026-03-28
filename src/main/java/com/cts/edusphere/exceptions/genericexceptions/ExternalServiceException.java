package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a call to an external third-party service fails unexpectedly.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ExternalServiceException extends RuntimeException{

    /**
     * Constructs a new {@code ExternalServiceException} with the specified detail message.
     *
     * @param message the detail message describing the external service failure
     */
    public ExternalServiceException(String message){
        super(message);
    }
}
