package com.cts.edusphere.exceptions.genericexceptions;

public class CannotDeleteException extends RuntimeException {
    public CannotDeleteException(String message) {
        super(message);
    }
}
