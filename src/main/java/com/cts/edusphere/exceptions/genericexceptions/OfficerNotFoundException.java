package com.cts.edusphere.exceptions.genericexceptions;

public class OfficerNotFoundException extends RuntimeException{
    public OfficerNotFoundException(String mssg) {
        super(mssg);
    }
}
