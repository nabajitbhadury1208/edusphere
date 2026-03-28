package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of departments could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class DepartmentsNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code DepartmentsNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which departments were not found
     */
    public DepartmentsNotFoundException(String mssg) {
        super(mssg);
    }

}
