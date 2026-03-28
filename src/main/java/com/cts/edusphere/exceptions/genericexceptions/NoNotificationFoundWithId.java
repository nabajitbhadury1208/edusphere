package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a notification cannot be located by its unique identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class NoNotificationFoundWithId extends RuntimeException {

  /**
   * Constructs a new {@code NoNotificationFoundWithId} with the specified detail message.
   *
   * @param mssg the detail message identifying which notification was not found
   */
  public NoNotificationFoundWithId(String mssg) {
    super(mssg);
  }
}
