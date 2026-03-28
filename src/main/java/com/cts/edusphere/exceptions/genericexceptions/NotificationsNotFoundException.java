package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of notifications could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class NotificationsNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code NotificationsNotFoundException} with the specified detail message.
   *
   * @param mssg the detail message describing which notifications were not found
   */
  public NotificationsNotFoundException(String mssg) {
    super(mssg);
  }
}
