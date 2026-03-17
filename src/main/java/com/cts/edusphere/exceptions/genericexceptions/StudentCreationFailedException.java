package com.cts.edusphere.exceptions.genericexceptions;

public class StudentCreationFailedException extends RuntimeException {
    public StudentCreationFailedException(String mssg) {
        super(mssg);
    }
}
