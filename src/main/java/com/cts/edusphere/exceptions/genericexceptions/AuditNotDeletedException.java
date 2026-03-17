package com.cts.edusphere.exceptions.genericexceptions;

public class AuditNotDeletedException extends RuntimeException {
    public AuditNotDeletedException(String mssg) {
        super(mssg);
    }
}
