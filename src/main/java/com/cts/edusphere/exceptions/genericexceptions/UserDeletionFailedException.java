package com.cts.edusphere.exceptions.genericexceptions;

public class UserDeletionFailedException extends RuntimeException {
    public UserDeletionFailedException(String message) {
        super(message);
    }
}
