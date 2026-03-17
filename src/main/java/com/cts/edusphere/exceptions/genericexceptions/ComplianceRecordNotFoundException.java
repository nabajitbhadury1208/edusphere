package com.cts.edusphere.exceptions.genericexceptions;

public class ComplianceRecordNotFoundException extends RuntimeException{
    public ComplianceRecordNotFoundException(String mssg) {
        super(mssg);
    }
}