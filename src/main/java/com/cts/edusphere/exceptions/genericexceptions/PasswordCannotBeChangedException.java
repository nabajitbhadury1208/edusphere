package com.cts.edusphere.exceptions.genericexceptions;

public class PasswordCannotBeChangedException extends RuntimeException{
    public PasswordCannotBeChangedException(String message) {
        super(message);
    }
}
