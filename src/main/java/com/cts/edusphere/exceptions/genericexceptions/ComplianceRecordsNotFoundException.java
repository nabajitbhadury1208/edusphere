package com.cts.edusphere.exceptions.genericexceptions;

public class ComplianceRecordsNotFoundException extends RuntimeException {
    public ComplianceRecordsNotFoundException(String mssg) {
        super(mssg);
    }
}