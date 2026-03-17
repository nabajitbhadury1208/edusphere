package com.cts.edusphere.exceptions.genericexceptions;

public class AuditLogsNotFoundException extends RuntimeException {
    public AuditLogsNotFoundException(String mssg) {
        super(mssg);
    }
}
