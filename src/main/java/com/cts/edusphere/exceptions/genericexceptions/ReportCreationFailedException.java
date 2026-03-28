package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an institutional report could not be generated or persisted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ReportCreationFailedException extends RuntimeException {

  /**
   * Constructs a new {@code ReportCreationFailedException} with the specified detail message.
   *
   * @param mssg the detail message describing why the report could not be created
   */
  public ReportCreationFailedException(String mssg) {
    super(mssg);
  }

}
