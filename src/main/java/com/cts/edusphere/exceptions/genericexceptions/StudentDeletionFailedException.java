package com.cts.edusphere.exceptions.genericexceptions;

public class StudentDeletionFailedException extends RuntimeException {
    public StudentDeletionFailedException(String mssg) {
        super(mssg);
    }
    
}
