package com.cts.edusphere.exceptions.genericexceptions;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String mssg) {
        super(mssg);
    }
}

