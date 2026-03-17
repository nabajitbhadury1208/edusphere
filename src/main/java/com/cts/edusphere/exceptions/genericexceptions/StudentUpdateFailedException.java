package com.cts.edusphere.exceptions.genericexceptions;

public class StudentUpdateFailedException extends RuntimeException {
    public StudentUpdateFailedException(String mssg) {
        super(mssg);
    }
    
}
