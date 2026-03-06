package com.cts.edusphere.exceptions.genericexceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String mssg) {
        super(mssg);
    }
}
