package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a thesis record could not be persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ThesisCreationFailedException extends RuntimeException {

  /**
   * Constructs a new {@code ThesisCreationFailedException} with the specified detail message.
   *
   * @param mssg the detail message describing why the thesis could not be created
   */
  public ThesisCreationFailedException(String mssg) {
    super(mssg);
  }

}
