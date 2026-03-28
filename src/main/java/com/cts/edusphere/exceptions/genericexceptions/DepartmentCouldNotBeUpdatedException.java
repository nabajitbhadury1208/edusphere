package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a department could not be updated due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class DepartmentCouldNotBeUpdatedException extends RuntimeException {

    /**
     * Constructs a new {@code DepartmentCouldNotBeUpdatedException} with the specified detail message.
     *
     * @param mssg the detail message describing why the department could not be updated
     */
    public DepartmentCouldNotBeUpdatedException(String mssg) {
        super(mssg);
    }
}
