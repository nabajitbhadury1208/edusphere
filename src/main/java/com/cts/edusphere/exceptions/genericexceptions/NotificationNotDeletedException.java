package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a notification could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class NotificationNotDeletedException extends RuntimeException {

    /**
     * Constructs a new {@code NotificationNotDeletedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the notification could not be deleted
     */
    public NotificationNotDeletedException(String mssg) {
        super(mssg);
    }

}
