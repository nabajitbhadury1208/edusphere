package com.cts.edusphere.exceptions.genericexceptions;

public class UserUpdateFailedException extends RuntimeException {
    public UserUpdateFailedException(String message) {
        super(message);
    }
}
