package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific user account cannot be located by their identifier or email address.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code UserNotFoundException} with the specified detail message.
   *
   * @param mssg the detail message identifying which user was not found
   */
  public UserNotFoundException(String mssg) {
    super(mssg);
  }
}
