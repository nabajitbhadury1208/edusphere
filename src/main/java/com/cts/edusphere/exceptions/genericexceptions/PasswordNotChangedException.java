package com.cts.edusphere.exceptions.genericexceptions;

public class PasswordNotChangedException extends RuntimeException{
    public PasswordNotChangedException(String message) {
        super(message);
    }
}
