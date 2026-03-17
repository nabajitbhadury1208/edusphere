package com.cts.edusphere.exceptions.genericexceptions;

public class StudentsNotFoundException extends RuntimeException {
    public StudentsNotFoundException(String mssg) {
        super(mssg);
    }
}