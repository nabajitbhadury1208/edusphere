package com.cts.edusphere.exceptions.genericexceptions;

public class StudentDocumentDeletionFailedException extends RuntimeException {
    public StudentDocumentDeletionFailedException(String mssg) {
        super(mssg);
    }
    
}
