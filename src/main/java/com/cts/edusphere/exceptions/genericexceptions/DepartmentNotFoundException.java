package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a specific department cannot be located by its identifier.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class DepartmentNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code DepartmentNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message identifying which department was not found
     */
    public DepartmentNotFoundException(String mssg) {
        super(mssg);
    }
}
