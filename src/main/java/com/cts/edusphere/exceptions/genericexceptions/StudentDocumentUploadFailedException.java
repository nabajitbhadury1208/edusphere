package com.cts.edusphere.exceptions.genericexceptions;

public class StudentDocumentUploadFailedException extends RuntimeException {
    public StudentDocumentUploadFailedException(String mssg) {
        super(mssg);
    }
}
