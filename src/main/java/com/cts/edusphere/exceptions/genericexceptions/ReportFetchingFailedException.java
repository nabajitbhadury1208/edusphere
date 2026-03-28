package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when one or more institutional reports could not be retrieved due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ReportFetchingFailedException extends RuntimeException {

  /**
   * Constructs a new {@code ReportFetchingFailedException} with the specified detail message.
   *
   * @param mssg the detail message describing why the report(s) could not be fetched
   */
  public ReportFetchingFailedException(String mssg) {
    super(mssg);
  }

}
