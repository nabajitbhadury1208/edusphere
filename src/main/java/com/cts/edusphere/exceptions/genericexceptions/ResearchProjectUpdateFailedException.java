package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a research project could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ResearchProjectUpdateFailedException extends RuntimeException  {

  /**
   * Constructs a new {@code ResearchProjectUpdateFailedException} with the specified detail message.
   *
   * @param mssg the detail message describing why the research project could not be updated
   */
  public ResearchProjectUpdateFailedException(String mssg) {
    super(mssg);
  }

}
