package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a notification could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class NotificationNotCreatedException extends RuntimeException {

    /**
     * Constructs a new {@code NotificationNotCreatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the notification could not be created
     */
    public NotificationNotCreatedException(String mssg) {
        super(mssg);
    }
}
