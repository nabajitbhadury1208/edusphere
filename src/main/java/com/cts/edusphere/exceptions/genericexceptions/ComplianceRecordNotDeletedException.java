package com.cts.edusphere.exceptions.genericexceptions;

public class ComplianceRecordNotDeletedException extends RuntimeException {
    public ComplianceRecordNotDeletedException(String mssg) {
        super(mssg);
    }
}
