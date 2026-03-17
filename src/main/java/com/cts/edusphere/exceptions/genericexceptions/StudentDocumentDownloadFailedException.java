package com.cts.edusphere.exceptions.genericexceptions;

public class StudentDocumentDownloadFailedException extends RuntimeException {
    public StudentDocumentDownloadFailedException(String mssg) {
        super(mssg);
    }
    
}
