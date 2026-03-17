package com.cts.edusphere.exceptions.genericexceptions;

public class ExamsNotFoundException extends RuntimeException {
    public ExamsNotFoundException(String mssg) {
        super(mssg);
    }
}
