package com.cts.edusphere.exceptions.genericexceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String mssg) {
        super(mssg);
    }
}

