package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific notification cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class NotificationNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code NotificationNotFoundException} with the specified detail message.
   *
   * @param mssg the detail message identifying which notification was not found
   */
  public NotificationNotFoundException(String mssg) {
    super(mssg);
  }
}
