package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a research project could not be deleted due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class ResearchProjectDeletionFailed extends RuntimeException {

  /**
   * Constructs a new {@code ResearchProjectDeletionFailed} with the specified detail message.
   *
   * @param mssg the detail message describing why the research project could not be deleted
   */
  public ResearchProjectDeletionFailed(String mssg) {
    super(mssg);
  }

}
