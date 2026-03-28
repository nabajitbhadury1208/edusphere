package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a user could not be subscribed to a notification channel or topic
 * due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class SubscribingToNotificationFailed extends RuntimeException {

    /**
     * Constructs a new {@code SubscribingToNotificationFailed} with the specified detail message.
     *
     * @param mssg the detail message describing why the subscription failed
     */
    public SubscribingToNotificationFailed(String mssg) {
        super(mssg);
    }

}
