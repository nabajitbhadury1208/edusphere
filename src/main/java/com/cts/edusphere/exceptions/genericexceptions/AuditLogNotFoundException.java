package com.cts.edusphere.exceptions.genericexceptions;

public class AuditLogNotFoundException extends RuntimeException {
    public AuditLogNotFoundException(String mssg) {
        super(mssg);
    }
}
