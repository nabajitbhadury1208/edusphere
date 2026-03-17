package com.cts.edusphere.exceptions.genericexceptions;

public class ReportDeletionFailedException extends RuntimeException {

    public ReportDeletionFailedException(String mssg) {
        super(mssg);
    }
}