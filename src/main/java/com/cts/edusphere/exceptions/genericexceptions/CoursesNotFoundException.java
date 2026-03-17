package com.cts.edusphere.exceptions.genericexceptions;

public class CoursesNotFoundException extends RuntimeException {
    public CoursesNotFoundException(String mssg) {
        super(mssg);
    }
}