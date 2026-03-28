package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a notification could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class NotificationNotUpdatedException extends RuntimeException {

    /**
     * Constructs a new {@code NotificationNotUpdatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the notification could not be updated
     */
    public NotificationNotUpdatedException(String mssg) {
        super(mssg);
    }

}
