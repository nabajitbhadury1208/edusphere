package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific research project cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class ResearchProjectNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code ResearchProjectNotFoundException} with the specified detail message.
   *
   * @param mssg the detail message identifying which research project was not found
   */
  public ResearchProjectNotFoundException(String mssg) {
    super(mssg);
  }

}
