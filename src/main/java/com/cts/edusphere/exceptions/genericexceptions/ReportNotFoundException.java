package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific institutional report cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ReportNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code ReportNotFoundException} with the specified detail message.
   *
   * @param mssg the detail message identifying which report was not found
   */
  public ReportNotFoundException(String mssg) {
    super(mssg);
  }

}
