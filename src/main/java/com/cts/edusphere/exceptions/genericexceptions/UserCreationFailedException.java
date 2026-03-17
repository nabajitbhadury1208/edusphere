package com.cts.edusphere.exceptions.genericexceptions;

public class UserCreationFailedException extends RuntimeException {
    public UserCreationFailedException(String message) {
        super(message);
    }
}
