package com.cts.edusphere.exceptions.genericexceptions;

public class CourseNotDeletedException extends RuntimeException {
    public CourseNotDeletedException(String mssg) {
        super(mssg);
    }
}
