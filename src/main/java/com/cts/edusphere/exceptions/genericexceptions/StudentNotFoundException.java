package com.cts.edusphere.exceptions.genericexceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String mssg) {
        super(mssg);
    }
    
}
