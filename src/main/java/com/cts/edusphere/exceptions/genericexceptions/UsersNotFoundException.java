package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when a collection of user accounts could not be retrieved or is empty.
 *
 * <p>Mapped to {@code 404 Not Found} by the global exception handler.</p>
 */
public class UsersNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code UsersNotFoundException} with the specified detail message.
     *
     * @param mssg the detail message describing which users were not found
     */
    public UsersNotFoundException(String mssg) {
        super(mssg);
    }
}
