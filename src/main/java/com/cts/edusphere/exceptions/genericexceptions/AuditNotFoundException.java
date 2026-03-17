package com.cts.edusphere.exceptions.genericexceptions;

public class AuditNotFoundException extends RuntimeException {
    public AuditNotFoundException(String mssg) {
        super(mssg);
    }
}
